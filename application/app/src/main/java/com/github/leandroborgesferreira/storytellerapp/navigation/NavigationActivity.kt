package com.github.leandroborgesferreira.storytellerapp.navigation

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.storiesteller.sdk.network.injector.ApiInjector
import com.storiesteller.sdk.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storytellerapp.AndroidLogger
import com.storiesteller.sdkapp.account.navigation.accountMenuNavigation
import com.storiesteller.sdkapp.account.viewmodel.AccountMenuViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.core.di.AuthCoreInjection
import com.storiesteller.sdkapp.auth.core.token.AmplifyTokenHandler
import com.storiesteller.sdkapp.auth.di.AuthInjection
import com.storiesteller.sdkapp.auth.navigation.authNavigation
import com.storiesteller.sdkapp.auth.navigation.navigateToAuthMenu
import com.storiesteller.sdkapp.editor.di.EditorInjector
import com.storiesteller.sdkapp.editor.navigation.editorNavigation
import com.github.leandroborgesferreira.storytellerapp.note_menu.di.NotesMenuInjection
import com.storiesteller.sdkapp.note_menu.navigation.notesMenuNavigation
import com.github.leandroborgesferreira.storytellerapp.theme.ApplicationComposeTheme
import com.storiesteller.sdkapp.utils_module.Destinations

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavigationGraph(application = application)
        }
    }
}

@Composable
fun NavigationGraph(
    application: Application,
    navController: NavHostController = rememberNavController(),
    database: StoryTellerDatabase = StoryTellerDatabase.database(application, builder = {
        this.createFromAsset("StoryTellerDatabase.db")
    }),
    sharedPreferences: SharedPreferences = application.getSharedPreferences(
        "com.storiesteller.sdkapp.preferences",
        Context.MODE_PRIVATE
    ),
    startDestination: String = Destinations.AUTH_MENU_INNER_NAVIGATION.id
) {

    val apiInjector =
        ApiInjector(apiLogger = AndroidLogger, bearerTokenHandler = AmplifyTokenHandler)
    val authCoreInjection = AuthCoreInjection(sharedPreferences)
    val authInjection = AuthInjection(authCoreInjection, database, apiInjector)
    val editorInjector = EditorInjector(database, authCoreInjection)
    val notesMenuInjection =
        NotesMenuInjection(database, sharedPreferences, authCoreInjection.provideAccountManager())

    ApplicationComposeTheme {
        NavHost(navController = navController, startDestination = startDestination) {
            authNavigation(navController, authInjection, navController::navigateToMainMenu)

            notesMenuNavigation(
                notesMenuInjection = notesMenuInjection,
                navigateToNote = navController::navigateToNote,
                navigateToAccount = navController::navigateToAccount,
                navigateToNewNote = navController::navigateToNewNote
            )

            editorNavigation(
                editorInjector = editorInjector,
                navigateToNoteMenu = navController::navigateToNoteMenu
            )

            accountMenuNavigation(
                accountMenuViewModel = AccountMenuViewModel(
                    authCoreInjection.provideAccountManager(),
                    authCoreInjection.provideAuthRepository()
                ),
                navController::navigateToAuthMenu
            )
        }
    }
}

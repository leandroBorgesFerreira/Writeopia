package io.storiesteller.navigation

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
import io.storiesteller.sdk.network.injector.ApiInjector
import io.storiesteller.sdk.persistence.database.StoriesTellerDatabase
import io.storiesteller.AndroidLogger
import io.storiesteller.account.navigation.accountMenuNavigation
import io.storiesteller.account.viewmodel.AccountMenuViewModel
import io.storiesteller.auth.core.di.AuthCoreInjection
import io.storiesteller.auth.core.token.AmplifyTokenHandler
import io.storiesteller.auth.di.AuthInjection
import io.storiesteller.auth.navigation.authNavigation
import io.storiesteller.auth.navigation.navigateToAuthMenu
import io.storiesteller.editor.di.EditorInjector
import io.storiesteller.editor.navigation.editorNavigation
import io.storiesteller.note_menu.di.NotesMenuInjection
import io.storiesteller.note_menu.navigation.notesMenuNavigation
import io.storiesteller.theme.ApplicationComposeTheme
import io.storiesteller.utils_module.Destinations

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
    database: StoriesTellerDatabase = StoriesTellerDatabase.database(application, builder = {
        this.createFromAsset("StoriesTellerDatabase.db")
    }),
    sharedPreferences: SharedPreferences = application.getSharedPreferences(
        "io.storiesteller.preferences",
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

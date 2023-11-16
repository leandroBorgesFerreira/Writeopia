package io.writeopia.navigation

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
import io.writeopia.AndroidLogger
import io.writeopia.account.navigation.accountMenuNavigation
import io.writeopia.account.viewmodel.AccountMenuAndroidViewModel
import io.writeopia.account.viewmodel.AccountMenuKmpViewModel
import io.writeopia.auth.core.BuildConfig
import io.writeopia.auth.core.di.AndroidAuthCoreInjection
import io.writeopia.auth.core.token.AmplifyTokenHandler
import io.writeopia.auth.di.AuthInjection
import io.writeopia.auth.navigation.authNavigation
import io.writeopia.auth.navigation.navigateToAuthMenu
import io.writeopia.editor.di.EditorInjector
import io.writeopia.editor.navigation.editorNavigation
import io.writeopia.note_menu.di.NotesMenuInjection
import io.writeopia.note_menu.navigation.notesMenuNavigation
import io.writeopia.persistence.WriteopiaApplicationDatabase
import io.writeopia.persistence.injection.AppDaosInjection
import io.writeopia.persistence.injection.RoomDaosInjection
import io.writeopia.sdk.network.injector.ApiInjector
import io.writeopia.theme.ApplicationComposeTheme
import io.writeopia.utils_module.Destinations

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val startDestination = if (BuildConfig.DEBUG) {
                Destinations.CHOOSE_NOTE.id
            } else {
                Destinations.AUTH_MENU_INNER_NAVIGATION.id
            }

            NavigationGraph(application = application, startDestination = startDestination)
        }
    }
}

@Composable
fun NavigationGraph(
    application: Application,
    navController: NavHostController = rememberNavController(),
    sharedPreferences: SharedPreferences = application.getSharedPreferences(
        "io.writeopia.preferences",
        Context.MODE_PRIVATE
    ),
    database: WriteopiaApplicationDatabase = WriteopiaApplicationDatabase.database(application),
    startDestination: String = Destinations.AUTH_MENU_INNER_NAVIGATION.id
) {

    val appDaosInjection = AppDaosInjection(database)
    val apiInjector =
        ApiInjector(apiLogger = AndroidLogger, bearerTokenHandler = AmplifyTokenHandler)
    val authCoreInjection = AndroidAuthCoreInjection(sharedPreferences)
    val daosInjection = RoomDaosInjection(database)
    val authInjection = AuthInjection(authCoreInjection, apiInjector, daosInjection)
    val editorInjector = EditorInjector.create(authCoreInjection, daosInjection)
    val notesMenuInjection =
        NotesMenuInjection(
            appDaosInjection.provideConfigurationDao(),
            authCoreInjection.provideAccountManager(),
            daosInjection
        )

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
                accountMenuViewModel = AccountMenuAndroidViewModel(
                    AccountMenuKmpViewModel(
                        authCoreInjection.provideAccountManager(),
                        authCoreInjection.provideAuthRepository()
                    )
                ),
                navController::navigateToAuthMenu
            )
        }
    }
}

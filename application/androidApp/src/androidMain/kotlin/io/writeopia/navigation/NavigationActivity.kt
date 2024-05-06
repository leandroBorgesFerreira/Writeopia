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
import io.writeopia.BuildConfig
import io.writeopia.account.di.AndroidAccountMenuInjector
import io.writeopia.account.navigation.accountMenuNavigation
import io.writeopia.auth.core.di.AndroidAuthCoreInjection
import io.writeopia.auth.core.token.FirebaseTokenHandler
import io.writeopia.auth.di.AuthInjection
import io.writeopia.auth.navigation.authNavigation
import io.writeopia.auth.navigation.navigateToAuthMenu
import io.writeopia.editor.di.EditorInjector
import io.writeopia.editor.navigation.editorNavigation
import io.writeopia.navigation.notes.navigateToAccount
import io.writeopia.navigation.notes.navigateToNewNote
import io.writeopia.navigation.notes.navigateToNote
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.note_menu.di.NotesMenuAndroidInjection
import io.writeopia.note_menu.navigation.notesMenuNavigation
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.persistence.room.injection.AppRoomDaosInjection
import io.writeopia.persistence.room.injection.RoomRespositoryInjection
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.theme.WrieopiaTheme
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
    val appDaosInjection = AppRoomDaosInjection(database)
    val notesConfigurationInjector = NotesConfigurationInjector(appDaosInjection)
    val connectionInjector =
        ConnectionInjector(
            apiLogger = AndroidLogger,
            bearerTokenHandler = FirebaseTokenHandler,
            baseUrl = BuildConfig.BASE_URL
        )
    val authCoreInjection = AndroidAuthCoreInjection(sharedPreferences)
    val repositoryInjection = RoomRespositoryInjection(database)
    val authInjection = AuthInjection(authCoreInjection, connectionInjector, repositoryInjection)
    val editorInjector =
        EditorInjector.create(authCoreInjection, repositoryInjection, connectionInjector)
    val accountMenuInjector = AndroidAccountMenuInjector.create(authCoreInjection)
    val notesMenuInjection = NotesMenuAndroidInjection.create(
        notesConfigurationInjector,
        authCoreInjection,
        repositoryInjection
    )

    WrieopiaTheme {
        Navigation(
            notesMenuInjection = notesMenuInjection,
            navController = navController,
            editorInjector = editorInjector,
            accountMenuInjector = accountMenuInjector,
            startDestination = startDestination,
            isUndoKeyEvent = { false }
        ) {
            authNavigation(navController, authInjection, navController::navigateToMainMenu)
        }
    }
}

package io.writeopia.navigation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.writeopia.AndroidLogger
import io.writeopia.BuildConfig
import io.writeopia.account.di.AndroidAccountMenuInjector
import io.writeopia.auth.core.di.AndroidAuthCoreInjection
import io.writeopia.auth.core.token.FirebaseTokenHandler
import io.writeopia.auth.di.AuthInjection
import io.writeopia.auth.navigation.authNavigation
import io.writeopia.common.utils.Destinations
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.editor.di.EditorInjector
import io.writeopia.model.isDarkTheme
import io.writeopia.navigation.notes.navigateToNoteMenu
import io.writeopia.navigation.notifications.navigateToNotifications
import io.writeopia.navigation.search.navigateToSearch
import io.writeopia.notemenu.data.model.NotesNavigation
import io.writeopia.notemenu.di.NotesInjector
import io.writeopia.notemenu.di.NotesMenuAndroidInjection
import io.writeopia.notemenu.di.UiConfigurationInjector
import io.writeopia.notemenu.navigation.NoteMenuDestiny
import io.writeopia.notemenu.navigation.navigateToNotes
import io.writeopia.persistence.room.WriteopiaApplicationDatabase
import io.writeopia.persistence.room.injection.AppRoomDaosInjection
import io.writeopia.persistence.room.injection.RoomRespositoryInjection
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.theme.WrieopiaTheme

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val startDestination = if (BuildConfig.DEBUG) {
                NoteMenuDestiny.noteMenu()
            } else {
                Destinations.AUTH_MENU_INNER_NAVIGATION.id
            }

            NavigationGraph(application = application, startDestination = startDestination)
        }
    }
}

@SuppressLint("RestrictedApi")
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
    val notesInjector = NotesInjector(appDaosInjection)
    val connectionInjector = ConnectionInjector(
        apiLogger = AndroidLogger,
        bearerTokenHandler = FirebaseTokenHandler,
        baseUrl = BuildConfig.BASE_URL
    )
    val authCoreInjection = AndroidAuthCoreInjection(sharedPreferences)
    val repositoryInjection = RoomRespositoryInjection(database)
    val authInjection = AuthInjection(authCoreInjection, connectionInjector, repositoryInjection)
    val editorInjector = EditorInjector.create(
        authCoreInjection,
        repositoryInjection,
        connectionInjector
    )
    val accountMenuInjector = AndroidAccountMenuInjector.create(authCoreInjection)
    val notesMenuInjection = NotesMenuAndroidInjection.create(
        notesInjector,
        authCoreInjection,
        repositoryInjection,
    )

    val navigationViewModel = viewModel { NavigationViewModel() }
    val uiConfigViewModel =
        UiConfigurationInjector(sharedPreferences).provideUiConfigurationViewModel()
    val colorTheme by uiConfigViewModel.listenForColorTheme { "disconnected_user" }.collectAsState()

    WrieopiaTheme(darkTheme = colorTheme.isDarkTheme()) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val navigationItems by navigationViewModel.selectedNavigation.collectAsState()

                    navigationItems.forEach { item ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { destination ->
                                destination.route?.let {
                                    NavItemName.selectRoute(it)
                                }?.value == item.navItemName.value
                            } ?: false

                        NavigationBarItem(
                            selected = isSelected,
                            icon = {
                                Icon(
                                    imageVector = item.navItemName.iconForNavItem(),
                                    contentDescription = item.navItemName.value
                                )
                            },
                            onClick = {
                                navController.navigateToItem(item.navItemName) {
                                    if (!isSelected) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors()
                                .copy(
                                    selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedIndicatorColor = MaterialTheme.colorScheme.secondary,
                                )
                        )
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Navigation(
                    notesMenuInjection = notesMenuInjection,
                    navController = navController,
                    editorInjector = editorInjector,
                    accountMenuInjector = accountMenuInjector,
                    startDestination = startDestination,
                    selectColorTheme = uiConfigViewModel::changeColorTheme,
                    isUndoKeyEvent = { false },
                ) {
                    authNavigation(navController, authInjection) {
                        navController.navigateToNotes(NotesNavigation.Root)
                    }
                }
            }
        }
    }
}

private fun NavHostController.navigateToItem(
    navItem: NavItemName,
    builder: NavOptionsBuilder.() -> Unit
) {
    when (navItem) {
        NavItemName.HOME -> this.navigateToNoteMenu(NotesNavigation.Root, builder)
        NavItemName.SEARCH -> this.navigateToSearch(builder)
        NavItemName.NOTIFICATIONS -> this.navigateToNotifications(builder)
    }
}

private fun NavItemName.iconForNavItem(): ImageVector =
    when (this) {
        NavItemName.HOME -> WrIcons.home
        NavItemName.SEARCH -> WrIcons.search
        NavItemName.NOTIFICATIONS -> WrIcons.notifications
    }

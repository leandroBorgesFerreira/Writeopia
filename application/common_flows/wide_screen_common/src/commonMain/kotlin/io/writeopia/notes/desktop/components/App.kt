package io.writeopia.notes.desktop.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.writeopia.account.di.AccountMenuKmpInjector
import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.auth.core.token.MockTokenHandler
import io.writeopia.editor.di.EditorKmpInjector
import io.writeopia.global.shell.viewmodel.SideMenuViewModel
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.darkTheme
import io.writeopia.navigation.Navigation
import io.writeopia.navigation.notes.navigateToNote
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.model.NotesNavigationType
import io.writeopia.note_menu.di.NotesInjector
import io.writeopia.note_menu.di.NotesMenuKmpInjection
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.note_menu.ui.screen.menu.SideGlobalMenu
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.theme.WrieopiaTheme
import io.writeopia.utils_module.Destinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow


@Composable
fun App(
    notesInjector: NotesInjector,
    repositoryInjection: RepositoryInjector,
    uiConfigurationInjector: UiConfigurationInjector,
    disableWebsocket: Boolean = false,
    selectionState: StateFlow<Boolean>,
    colorThemeOption: StateFlow<ColorThemeOption?>,
    coroutineScope: CoroutineScope,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    selectColorTheme: (ColorThemeOption) -> Unit,
) {
    val authCoreInjection = remember { KmpAuthCoreInjection() }
    val connectionInjection =
        remember {
            ConnectionInjector(
                bearerTokenHandler = MockTokenHandler,
                baseUrl = "https://writeopia.io/api",
                disableWebsocket = disableWebsocket
            )
        }
    val editorInjector = remember {
        EditorKmpInjector(
            authCoreInjection = authCoreInjection,
            repositoryInjection = repositoryInjection,
            connectionInjection = connectionInjection
        )
    }
    val accountInjector = remember { AccountMenuKmpInjector(authCoreInjection) }

    val notesMenuInjection = remember {
        NotesMenuKmpInjection(
            notesInjector = notesInjector,
            authCoreInjection = authCoreInjection,
            repositoryInjection = repositoryInjection,
            uiConfigurationInjector = uiConfigurationInjector,
            selectionState = selectionState,
        )
    }

    val sideMenuViewModel: SideMenuViewModel = null!!
    val colorTheme = colorThemeOption.collectAsState().value

    if (colorTheme != null) {
        WrieopiaTheme(darkTheme = colorThemeOption.collectAsState().value?.darkTheme() ?: false) {
            val showOptions by sideMenuViewModel.showSideMenu.collectAsState()
            val navigationController: NavHostController = rememberNavController()
            //Here!
            Row {
                SideGlobalMenu(
                    modifier = Modifier.fillMaxHeight(),
                    foldersState = sideMenuViewModel.sideMenuItems,
                    showOptions = showOptions,
                    width = 280.dp,
                    homeClick = { navigationController.navigateToNotes(NotesNavigation.Root) },
                    favoritesClick = {
                        navigationController.navigateToNotes(NotesNavigation.Favorites)
                    },
                    settingsClick = sideMenuViewModel::showSettings,
                    addFolder = sideMenuViewModel::addFolder,
                    editFolder = sideMenuViewModel::editFolder,
                    navigateToFolder = { id ->
                        navigationController.navigateToNotes(NotesNavigation.Folder(id))
                    },
                    navigateToEditDocument = navigationController::navigateToNote,
                    moveRequest = sideMenuViewModel::moveToFolder,
                    expandFolder = sideMenuViewModel::expandFolder,
                    highlightContent = sideMenuViewModel::highlightMenuItem
                )

                Navigation(
                    startDestination = startDestination(),
                    notesMenuInjection = notesMenuInjection,
                    accountMenuInjector = accountInjector,
                    coroutineScope = coroutineScope,
                    editorInjector = editorInjector,
                    isUndoKeyEvent = isUndoKeyEvent,
                    selectColorTheme = selectColorTheme,
                    navController = navigationController
                ) {}
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

private fun NavHostController.navigateToNotes(navigation: NotesNavigation) {
    when (navigation) {
        is NotesNavigation.Folder -> navigate(
            "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/${navigation.id}",
        )

        NotesNavigation.Favorites, NotesNavigation.Root -> navigate(
            "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/path",
        )
    }
}

private fun startDestination() =
    "${Destinations.CHOOSE_NOTE.id}/${NotesNavigationType.ROOT.type}/path"

package io.writeopia.notes.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.writeopia.account.di.AccountMenuKmpInjector
import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.auth.core.token.MockTokenHandler
import io.writeopia.editor.di.EditorKmpInjector
import io.writeopia.global.shell.SideGlobalMenu
import io.writeopia.global.shell.di.SideMenuKmpInjector
import io.writeopia.global.shell.viewmodel.GlobalShellViewModel
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.darkTheme
import io.writeopia.navigation.Navigation
import io.writeopia.navigation.notes.navigateToNote
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.model.NotesNavigationType
import io.writeopia.note_menu.data.usecase.NotesNavigationUseCase
import io.writeopia.note_menu.di.NotesInjector
import io.writeopia.note_menu.di.NotesMenuKmpInjection
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.note_menu.navigation.NAVIGATION_PATH
import io.writeopia.note_menu.navigation.NAVIGATION_TYPE
import io.writeopia.note_menu.ui.screen.configuration.modifier.icon
import io.writeopia.note_menu.ui.screen.menu.EditFileScreen
import io.writeopia.note_menu.ui.screen.menu.RoundedVerticalDivider
import io.writeopia.note_menu.ui.screen.settings.SettingsDialog
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.theme.WrieopiaTheme
import io.writeopia.utils_module.Destinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
            selectionState = selectionState,
        )
    }

    val sideMenuInjector = remember {
        SideMenuKmpInjector(
            notesInjector,
            authCoreInjection,
            repositoryInjection,
            uiConfigurationInjector,
            selectionState
        )
    }

    val globalShellViewModel: GlobalShellViewModel =
        sideMenuInjector.provideSideMenuViewModel(coroutineScope)
    val colorTheme = colorThemeOption.collectAsState().value
    val navigationController: NavHostController = rememberNavController()
    val showOptions by globalShellViewModel.showSideMenu.collectAsState()

    coroutineScope.launch {
        navigationController.currentBackStackEntryFlow.collect { navEntry ->
            val navigationType = navEntry.arguments?.getString(NAVIGATION_TYPE)
            val navigationPath = navEntry.arguments?.getString(NAVIGATION_PATH)
            if (navigationType != null && navigationPath != null) {
                NotesNavigation.fromType(
                    NotesNavigationType.fromType(navigationType),
                    navigationPath
                ).let(NotesNavigationUseCase.singleton()::setNoteNavigation)
            }
        }
    }

    WrieopiaTheme(darkTheme = colorTheme.darkTheme()) {
        Row(Modifier.background(MaterialTheme.colorScheme.background)) {
            SideGlobalMenu(
                modifier = Modifier.fillMaxHeight(),
                foldersState = globalShellViewModel.sideMenuItems,
                showOptions = showOptions,
                width = 280.dp,
                homeClick = { navigationController.navigateToNotes(NotesNavigation.Root) },
                favoritesClick = {
                    navigationController.navigateToNotes(NotesNavigation.Favorites)
                },
                settingsClick = globalShellViewModel::showSettings,
                addFolder = globalShellViewModel::addFolder,
                editFolder = globalShellViewModel::editFolder,
                navigateToFolder = { id ->
                    val navigation = NotesNavigation.Folder(id)
                    navigationController.navigateToNotes(navigation)
                },
                navigateToEditDocument = navigationController::navigateToNote,
                moveRequest = globalShellViewModel::moveToFolder,
                expandFolder = globalShellViewModel::expandFolder,
                highlightContent = {}
            )

            Column {
                GlobalHeader(navigationController, globalShellViewModel.folderPath)

                Box {
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

                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .width(16.dp)
                            .align(alignment = Alignment.CenterStart)
                            .clip(RoundedCornerShape(100))
                            .clickable(onClick = globalShellViewModel::toggleSideMenu)
                            .padding(vertical = 6.dp),
                    ) {
                        RoundedVerticalDivider(
                            modifier = Modifier.height(60.dp).align(Alignment.Center),
                            thickness = 4.dp,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }

                    val folderEdit = globalShellViewModel.editFolderState.collectAsState().value

                    if (folderEdit != null) {
                        EditFileScreen(
                            folderEdit = folderEdit,
                            onDismissRequest = globalShellViewModel::stopEditingFolder,
                            deleteFolder = globalShellViewModel::deleteFolder,
                            editFolder = globalShellViewModel::updateFolder
                        )
                    }

                    val showSettingsState by globalShellViewModel.showSettingsState.collectAsState()

                    if (showSettingsState) {
                        SettingsDialog(
                            selectedThemePosition = MutableStateFlow(2),
                            onDismissRequest = globalShellViewModel::hideSettings,
                            selectColorTheme = selectColorTheme
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlobalHeader(
    navigationController: NavHostController,
    pathState: StateFlow<List<String>>
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.icon {
                if (navigationController.previousBackStackEntry != null) {
                    navigationController.navigateUp()
                }
            },
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "Navigate back",
            tint = MaterialTheme.colorScheme.onBackground
        )

        PathToCurrentDirectory(pathState)
    }
}


@Composable
private fun PathToCurrentDirectory(pathState: StateFlow<List<String>>) {
    val path by pathState.collectAsState()
    val size = path.lastIndex

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 12.dp)
    ) {
        path.forEachIndexed { i, nodePath ->
            Text(
                text = nodePath,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            if (i != size) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
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

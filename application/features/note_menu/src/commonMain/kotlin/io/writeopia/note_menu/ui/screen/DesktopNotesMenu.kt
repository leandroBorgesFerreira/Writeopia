package io.writeopia.note_menu.ui.screen

import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.note_menu.ui.screen.actions.DesktopNoteActionsMenu
import io.writeopia.note_menu.ui.screen.configuration.modifier.icon
import io.writeopia.note_menu.ui.screen.configuration.molecules.NotesConfigurationMenu
import io.writeopia.note_menu.ui.screen.configuration.molecules.NotesSelectionMenu
import io.writeopia.note_menu.ui.screen.configuration.molecules.WorkspaceConfigurationDialog
import io.writeopia.note_menu.ui.screen.file.fileChooserLoad
import io.writeopia.note_menu.ui.screen.file.fileChooserSave
import io.writeopia.note_menu.ui.screen.documents.NotesCards
import io.writeopia.note_menu.ui.screen.menu.RoundedVerticalDivider
import io.writeopia.note_menu.ui.screen.menu.SideGlobalMenu
import io.writeopia.note_menu.ui.screen.settings.SettingsDialog
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.note_menu.viewmodel.ConfigState
import io.writeopia.note_menu.viewmodel.getPath
import io.writeopia.note_menu.viewmodel.toNumberDesktop
import io.writeopia.ui.draganddrop.target.DraggableScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DesktopNotesMenu(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigationController: NavController,
    onNewNoteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
    navigateToNotes: (NotesNavigation) -> Unit,
    addFolder: () -> Unit,
    editFolder: (MenuItemUi.FolderUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(
        key1 = "refresh",
        block = {
            chooseNoteViewModel.requestUser()
        }
    )

    val borderPadding = 8.dp

    DraggableScreen(modifier = modifier.fillMaxSize().padding(end = 12.dp)) {
        val showOptions by chooseNoteViewModel.showSideMenu.collectAsState()

        Row {
            SideGlobalMenu(
                modifier = Modifier.fillMaxHeight(),
                foldersState = chooseNoteViewModel.sideMenuItems,
                background = MaterialTheme.colorScheme.surfaceVariant,
                showOptions = showOptions,
                width = 280.dp,
                homeClick = { navigateToNotes(NotesNavigation.Root) },
                favoritesClick = { navigateToNotes(NotesNavigation.Favorites) },
                settingsClick = chooseNoteViewModel::showSettings,
                addFolder = addFolder,
                editFolder = editFolder,
                navigateToFolder = { id -> navigateToNotes(NotesNavigation.Folder(id)) },
                moveRequest = chooseNoteViewModel::moveToFolder,
                expandFolder = chooseNoteViewModel::expandFolder
            )

            Box {
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(16.dp)
                        .align(alignment = Alignment.CenterStart)
                        .clip(RoundedCornerShape(100))
                        .clickable(onClick = chooseNoteViewModel::toggleSideMenu)
                        .padding(vertical = 6.dp),
                ) {
                    RoundedVerticalDivider(
                        modifier = Modifier.height(60.dp).align(Alignment.Center),
                        thickness = 4.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                Column(modifier = Modifier.padding(top = borderPadding)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(borderPadding))

                        Icon(
                            modifier = Modifier.icon {
                                navigationController.navigateUp()
                            },
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        Icon(
                            modifier = Modifier.icon { },
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = "Navigate forward",
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.weight(1F))

                        DesktopNoteActionsMenu(
                            showExtraOptions = chooseNoteViewModel.editState,
                            showExtraOptionsRequest = chooseNoteViewModel::showEditMenu,
                            hideExtraOptionsRequest = chooseNoteViewModel::cancelEditMenu,
                            configureDirectory = chooseNoteViewModel::configureDirectory,
                            exportAsMarkdownClick = {
                                fileChooserSave("")?.let(chooseNoteViewModel::directoryFilesAsMarkdown)
                            },
                            importClick = {
                                chooseNoteViewModel.loadFiles(fileChooserLoad(""))
                            },
                            syncInProgressState = chooseNoteViewModel.syncInProgress,
                            onSyncLocallySelected = chooseNoteViewModel::onSyncLocallySelected,
                            onWriteLocallySelected = chooseNoteViewModel::onWriteLocallySelected,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 40.dp)
                    ) {
                        NotesCards(
                            documents = chooseNoteViewModel.documentsState.collectAsState().value,
                            loadNote = { id, title ->
                                val handled = chooseNoteViewModel.handleNoteTap(id)
                                if (!handled) {
                                    onNoteClick(id, title)
                                }
                            } ,
                            selectionListener = chooseNoteViewModel::onDocumentSelected,
                            folderClick = { id ->
                                navigateToNotes(NotesNavigation.Folder(id))
                            },
                            moveRequest = { item, parentId ->
                                chooseNoteViewModel.moveToFolder(item, parentId)
                            } ,
                            modifier = Modifier.weight(1F).fillMaxHeight()
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        NotesConfigurationMenu(
                            modifier = Modifier.padding(end = borderPadding),
                            showSortingOption = chooseNoteViewModel.showSortMenuState,
                            selectedState = chooseNoteViewModel.notesArrangement.toNumberDesktop(),
                            showSortOptionsRequest = chooseNoteViewModel::showSortMenu,
                            hideSortOptionsRequest = chooseNoteViewModel::cancelSortMenu,
                            staggeredGridSelected =
                            chooseNoteViewModel::staggeredGridArrangementSelected,
                            gridSelected = chooseNoteViewModel::gridArrangementSelected,
                            listSelected = chooseNoteViewModel::listArrangementSelected,
                            selectSortOption = chooseNoteViewModel::sortingSelected,
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(horizontal = 40.dp - borderPadding, vertical = 40.dp)
                .testTag("addNote"),
            onClick = onNewNoteClick,
            content = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New note",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            containerColor = MaterialTheme.colorScheme.primary
        )

        val configState = chooseNoteViewModel.showLocalSyncConfigState.collectAsState().value

        if (configState != ConfigState.Idle) {
            WorkspaceConfigurationDialog(
                currentPath = configState.getPath(),
                pathChange = chooseNoteViewModel::pathSelected,
                onDismissRequest = chooseNoteViewModel::hideConfigSyncMenu,
                onConfirmation = chooseNoteViewModel::confirmWorkplacePath
            )
        }

        val hasSelectedNotes by chooseNoteViewModel.hasSelectedNotes.collectAsState()

        NotesSelectionMenu(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp).width(400.dp),
            visibilityState = hasSelectedNotes,
            onDelete = chooseNoteViewModel::deleteSelectedNotes,
            onCopy = chooseNoteViewModel::copySelectedNotes,
            onFavorite = chooseNoteViewModel::favoriteSelectedNotes,
            shape = RoundedCornerShape(CornerSize(16.dp)),
            exitAnimationOffset = 2.3F,
            animationSpec = spring(dampingRatio = 0.6F)
        )

        val showSettingsState by chooseNoteViewModel.showSettingsState.collectAsState()

        if (showSettingsState) {
            SettingsDialog(
                selectedThemePosition = MutableStateFlow(2),
                onDismissRequest = chooseNoteViewModel::hideSettings,
                selectColorTheme = selectColorTheme
            )
        }
    }
}

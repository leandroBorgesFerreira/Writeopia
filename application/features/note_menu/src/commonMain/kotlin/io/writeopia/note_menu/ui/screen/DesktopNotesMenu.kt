package io.writeopia.note_menu.ui.screen

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.ui.screen.actions.DesktopNoteActionsMenu
import io.writeopia.note_menu.ui.screen.configuration.modifier.icon
import io.writeopia.note_menu.ui.screen.configuration.molecules.NotesConfigurationMenu
import io.writeopia.note_menu.ui.screen.configuration.molecules.NotesSelectionMenu
import io.writeopia.note_menu.ui.screen.configuration.molecules.WorkspaceConfigurationDialog
import io.writeopia.note_menu.ui.screen.file.fileChooserLoad
import io.writeopia.note_menu.ui.screen.file.fileChooserSave
import io.writeopia.note_menu.ui.screen.list.NotesCards
import io.writeopia.note_menu.ui.screen.menu.RoundedVerticalDivider
import io.writeopia.note_menu.ui.screen.menu.SideGlobalMenu
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.note_menu.viewmodel.ConfigState
import io.writeopia.note_menu.viewmodel.getPath
import io.writeopia.note_menu.viewmodel.toNumberDesktop

@Composable
fun DesktopNotesMenu(
    chooseNoteViewModel: ChooseNoteViewModel,
    onNewNoteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(
        key1 = "refresh",
        block = {
            chooseNoteViewModel.requestUser()
            chooseNoteViewModel.requestDocuments(false)
        }
    )

    val borderPadding = 8.dp

    Box(modifier = modifier.fillMaxSize().padding(end = 12.dp)) {
        var showOptions by remember { mutableStateOf(true) }

        Row {
            SideGlobalMenu(
                modifier = Modifier.fillMaxHeight(),
                background = MaterialTheme.colorScheme.surfaceVariant,
                showOptions = showOptions,
                width = 280.dp
            )

            Box {
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(16.dp)
                        .align(alignment = Alignment.CenterStart)
                        .clip(RoundedCornerShape(100))
                        .clickable { showOptions = !showOptions }
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
                            modifier = Modifier.icon { },
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        Icon(
                            modifier = Modifier.icon { },
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = "Settings",
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
                            loadNote = onNoteClick,
                            selectionListener = chooseNoteViewModel::onDocumentSelected,
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
    }
}

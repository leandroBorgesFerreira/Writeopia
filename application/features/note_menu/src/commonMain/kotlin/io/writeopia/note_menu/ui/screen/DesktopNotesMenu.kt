package io.writeopia.note_menu.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.ui.screen.actions.DesktopNoteActionsMenu
import io.writeopia.note_menu.ui.screen.configuration.WorkspaceConfigurationDialog
import io.writeopia.note_menu.ui.screen.file.fileChooserLoad
import io.writeopia.note_menu.ui.screen.file.fileChooserSave
import io.writeopia.note_menu.ui.screen.list.NotesCards
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.note_menu.viewmodel.ConfigState
import io.writeopia.note_menu.viewmodel.getPath

@Composable
fun DesktopNotesMenu(
    modifier: Modifier = Modifier,
    chooseNoteViewModel: ChooseNoteViewModel,
    onNewNoteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit
) {
    LaunchedEffect(
        key1 = "refresh",
        block = {
            chooseNoteViewModel.requestUser()
            chooseNoteViewModel.requestDocuments(false)
        }
    )

    Box(
        modifier = modifier
            .padding(start = 40.dp, end = 12.dp, bottom = 40.dp, top = 12.dp)
            .fillMaxSize()
    ) {
        Column {
            val showExtraOptions by chooseNoteViewModel.editState.collectAsState()

            DesktopNoteActionsMenu(
                modifier = Modifier.align(Alignment.End),
                showExtraOptions = showExtraOptions,
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

            NotesCards(
                documents = chooseNoteViewModel.documentsState.collectAsState().value,
                loadNote = onNoteClick,
                selectionListener = chooseNoteViewModel::onDocumentSelected,
            )
        }

        Column(modifier = Modifier.align(Alignment.BottomEnd).padding(horizontal = 30.dp)) {
            FloatingActionButton(
                modifier = Modifier.testTag("addNote"),
                onClick = onNewNoteClick,
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New note")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FloatingActionButton(
                modifier = Modifier.testTag("deleteNotes"),
                onClick = onDeleteClick,
                content = {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            )
        }

        val configState = chooseNoteViewModel.showLocalSyncConfigState.collectAsState().value

        if (configState != ConfigState.Idle) {
            WorkspaceConfigurationDialog(
                currentPath = configState.getPath(),
                pathChange = chooseNoteViewModel::pathSelected,
                onDismissRequest = chooseNoteViewModel::hideConfigSyncMenu,
                onConfirmation = {
                    chooseNoteViewModel.confirmWorkplacePath()
                }
            )
        }
    }
}
package io.writeopia.note_menu.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.ui.screen.actions.DesktopNoteActionsMenu
import io.writeopia.note_menu.ui.screen.configuration.WorkspaceConfigurationDialog
import io.writeopia.note_menu.ui.screen.file.fileChooserLoad
import io.writeopia.note_menu.ui.screen.file.fileChooserSave
import io.writeopia.note_menu.ui.screen.list.NotesCards
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel

@Composable
fun DesktopNotesMenu(
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
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp, bottom = 40.dp, top = 6.dp)
            .fillMaxSize()
    ) {
        Column {
            val syncButtonShape = RoundedCornerShape(10.dp)

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val showSyncLoading by chooseNoteViewModel.syncInProgress.collectAsState()

                if (showSyncLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.then(Modifier.size(20.dp)),
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    "Sync locally",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier
                        .clip(syncButtonShape)
                        .clickable(onClick = chooseNoteViewModel::onSyncLocallySelected)
                        .border(width = 2.dp, color = Color.Black, shape = syncButtonShape)
                        .padding(8.dp)
                )
            }


            val showExtraOptions by chooseNoteViewModel.editState.collectAsState()

            DesktopNoteActionsMenu(
                modifier = Modifier.align(Alignment.End),
                showExtraOptions = showExtraOptions,
                showExtraOptionsRequest = chooseNoteViewModel::showEditMenu,
                hideExtraOptionsRequest = chooseNoteViewModel::cancelEditMenu,
                exportAsMarkdownClick = {
                    fileChooserSave("")?.let(chooseNoteViewModel::directoryFilesAsMarkdown)

                },
                exportAsJsonClick = {
                    fileChooserSave("")?.let(chooseNoteViewModel::directoryFilesAsJson)
                },
                importClick = {
                    chooseNoteViewModel.loadFiles(fileChooserLoad(""))
                }
            )

            NotesCards(
                documents = chooseNoteViewModel.documentsState.collectAsState().value,
                loadNote = onNoteClick,
                selectionListener = chooseNoteViewModel::onDocumentSelected,
            )
        }

        Column(modifier = Modifier.align(Alignment.BottomEnd)) {
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

        val configState by chooseNoteViewModel.showLocalSyncConfigState.collectAsState()

        if (configState) {
            WorkspaceConfigurationDialog(
                onDismissRequest = chooseNoteViewModel::hideConfigSyncMenu,
                onConfirmation = {
                    fileChooserSave("")?.let(chooseNoteViewModel::selectedWorkplacePath)
                }
            )
        }
    }
}
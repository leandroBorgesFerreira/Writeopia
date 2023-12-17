package io.writeopia.note_menu.ui.desktop

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.ui.screen.menu.Notes
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel

@Composable
fun NotesMenu(
    chooseNoteViewModel: ChooseNoteViewModel,
    onNewNoteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit
) {
    LaunchedEffect(key1 = "refresh", block = {
        chooseNoteViewModel.requestUser()
        chooseNoteViewModel.requestDocuments(false)
    })

    Box {
        Notes(
            documents = chooseNoteViewModel.documentsState.collectAsState().value,
            loadNote = onNoteClick,
            selectionListener = chooseNoteViewModel::onDocumentSelected,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.align(Alignment.BottomEnd).padding(36.dp)) {
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
    }
}
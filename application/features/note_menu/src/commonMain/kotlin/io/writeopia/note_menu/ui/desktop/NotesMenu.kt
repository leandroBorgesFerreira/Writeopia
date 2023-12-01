package io.writeopia.note_menu.ui.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.ui.screen.menu.Notes
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel

@Composable
fun NotesMenu(
    chooseNoteViewModel: ChooseNoteViewModel,
    onNewNoteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit
) {
    LaunchedEffect(key1 = "refresh", block = {
        chooseNoteViewModel.requestUser()
        chooseNoteViewModel.requestDocuments(false)
        // Todo: Remove BuildConfig.DEBUG check later.
    })

    Box {
        Notes(
            documents = chooseNoteViewModel.documentsState.collectAsState().value,
            loadNote = onNoteClick,
            selectionListener = { _, _ -> },
            modifier = Modifier.fillMaxSize()
        )

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(36.dp),
            onClick = onNewNoteClick,
            content = {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New note")
            }
        )
    }
}
package io.writeopia.note_menu.ui.screen.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel

@Composable
expect fun NotesMenuScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    onNewNoteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
)

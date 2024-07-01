package io.writeopia.note_menu.ui.screen.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel

@Composable
actual fun NotesMenuScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigationController: NavController,
    onNewNoteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit,
    onAccountClick: () -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
    navigateToNotes: (NotesNavigation) -> Unit,
    addFolder: () -> Unit,
    editFolder: () -> Unit,
    modifier: Modifier
) {
    ChooseNoteScreen(
        chooseNoteViewModel = chooseNoteViewModel,
        navigateToNote = onNoteClick,
        newNote = onNewNoteClick,
        navigateToAccount = onAccountClick,
        modifier = modifier,
    )
}

package io.writeopia.note_menu.ui.screen.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel

@Composable
expect fun NotesMenuScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigationController: NavController,
    onNewNoteClick: () -> Unit,
    onNoteClick: (String, String) -> Unit,
    onAccountClick: () -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
    navigateToNotes: (NotesNavigation) -> Unit,
    modifier: Modifier = Modifier,
)

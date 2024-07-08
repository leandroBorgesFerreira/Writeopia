package io.writeopia.note_menu.ui.screen.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.ui.screen.DesktopNotesMenu
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
    editFolder: (Folder) -> Unit,
    modifier: Modifier
) {
    DesktopNotesMenu(
        chooseNoteViewModel = chooseNoteViewModel,
        navigationController = navigationController,
        onNewNoteClick = onNewNoteClick,
        onNoteClick = onNoteClick,
        selectColorTheme = selectColorTheme,
        navigateToNotes = navigateToNotes,
        addFolder = addFolder,
        editFolder = editFolder,
        modifier = modifier,
    )

    val folderEdit = chooseNoteViewModel.editFolderState.collectAsState().value

    if (folderEdit != null) {
        EditFileScreen(
            folderEdit = folderEdit,
            onDismissRequest = chooseNoteViewModel::stopEditingFolder,
            deleteFolder = chooseNoteViewModel::deleteFolder,
            editFolder = chooseNoteViewModel::updateFolder
        )
    }
}

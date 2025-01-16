package io.writeopia.notemenu.ui.screen.menu

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.writeopia.notemenu.viewmodel.ChooseNoteViewModel

@Composable
internal fun ChooseNoteScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    newNote: () -> Unit,
    navigateToAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasSelectedNotes by chooseNoteViewModel.hasSelectedNotes.collectAsState()
    val editState by chooseNoteViewModel.editState.collectAsState()

    BackHandler(hasSelectedNotes || editState) {
        when {
            editState -> {
                chooseNoteViewModel.cancelEditMenu()
            }

            hasSelectedNotes -> {
                chooseNoteViewModel.clearSelection()
            }
        }
    }

    MobileChooseNoteScreen(
        chooseNoteViewModel,
        navigateToNote,
        newNote,
        navigateToAccount,
        modifier
    )
}

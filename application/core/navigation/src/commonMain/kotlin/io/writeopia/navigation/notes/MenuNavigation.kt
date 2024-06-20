package io.writeopia.navigation.notes

import androidx.navigation.NavController
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.utils_module.Destinations

fun NavController.navigateToNoteMenu(notesNavigation: NotesNavigation) {
    navigate("${Destinations.CHOOSE_NOTE.id}/${notesNavigation.navigationType}/path")
}

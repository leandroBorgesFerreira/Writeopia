package io.writeopia.navigation.notes

import androidx.navigation.NavController
import io.writeopia.note_menu.data.usecase.NotesNavigationUseCase
import io.writeopia.utils_module.Destinations

fun NavController.navigateToNewNote() {
    val folderId = NotesNavigationUseCase.singleton().navigationState.value.id
    navigate("${Destinations.EDITOR.id}/$folderId")
}

fun NavController.navigateToNote(id: String, title: String) {
//    val noteId = this.currentBackStackEntry?.arguments?.getString("noteId")

//    if (noteId != id) {
    navigate("${Destinations.EDITOR.id}/$id/$title")
//    }
}

fun NavController.navigateToAccount() {
    navigate(Destinations.ACCOUNT.id)
}

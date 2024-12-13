package io.writeopia.navigation.notes

import androidx.navigation.NavController
import io.writeopia.common.utils.Destinations
import io.writeopia.notemenu.data.model.NotesNavigation
import io.writeopia.notemenu.data.usecase.NotesNavigationUseCase

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

fun NavController.navigateToFolder(navigation: NotesNavigation) {
    when (navigation) {
        is NotesNavigation.Folder -> this.navigate(
            "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/${navigation.id}",
        )

        NotesNavigation.Favorites, NotesNavigation.Root -> this.navigate(
            "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/path",
        )
    }
}

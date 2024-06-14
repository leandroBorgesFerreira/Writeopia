package io.writeopia.note_menu.data.model

sealed interface NotesNavigation {
    data object Favorites
    data class Folder(val folderId: String)
}

package io.writeopia.note_menu.data.model

sealed class NotesNavigation(val navigationType: NotesNavigationType) {
    data object Favorites : NotesNavigation(navigationType = NotesNavigationType.FAVORITES)
    data class Folder(val folderId: String) :
        NotesNavigation(navigationType = NotesNavigationType.FOLDER)

    data object Root : NotesNavigation(navigationType = NotesNavigationType.ROOT)

    companion object {
        fun fromType(type: NotesNavigationType, path: String): NotesNavigation =
            when (type) {
                NotesNavigationType.FAVORITES -> Favorites
                NotesNavigationType.ROOT -> Root
                NotesNavigationType.FOLDER -> Folder(path)
            }
    }
}

enum class NotesNavigationType(val type: String) {
    FAVORITES("favorites"), ROOT("root"), FOLDER("folder");

    companion object {
        fun fromType(type: String) = entries.firstOrNull { it.type == type }
            ?: throw IllegalArgumentException("Could not find type: $type")
    }
}



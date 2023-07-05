package br.com.leandroferreira.note_menu.viewmodel

enum class NotesArrangement(val type: String) {
    LIST("list"), GRID("grid");

    companion object {

        fun fromString(string: String): NotesArrangement =
            values().firstOrNull { notesArrangement ->
                notesArrangement.type == string
            } ?: throw IllegalArgumentException("not a NotesArrangement")

    }
}

package io.writeopia.core.configuration.models

/**
 * The possible arrangement for notes. The user is able to choose between the variants of the enum
 * changing the way notes present themselves in the menu.
 */
enum class NotesArrangement(val type: String) {
    LIST("list"),
    GRID("grid"),
    STAGGERED_GRID("staggered_grid");

    companion object {
        fun fromString(string: String): NotesArrangement =
            entries.firstOrNull { notesArrangement ->
                notesArrangement.type == string
            } ?: throw IllegalArgumentException("not a NotesArrangement: $string")
    }
}

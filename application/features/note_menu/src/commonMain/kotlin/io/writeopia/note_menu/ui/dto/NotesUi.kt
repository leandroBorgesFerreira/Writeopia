package io.writeopia.note_menu.ui.dto

import io.writeopia.note_menu.data.NotesArrangement

data class NotesUi(
    val documentUiList: List<DocumentUi>,
    val notesArrangement: NotesArrangement
)

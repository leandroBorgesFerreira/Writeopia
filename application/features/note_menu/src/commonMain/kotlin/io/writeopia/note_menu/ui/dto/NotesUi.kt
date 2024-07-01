package io.writeopia.note_menu.ui.dto

import io.writeopia.note_menu.data.model.NotesArrangement

data class NotesUi(
    val documentUiList: List<MenuItemUi>,
    val notesArrangement: NotesArrangement
)

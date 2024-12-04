package io.writeopia.notemenu.ui.dto

import io.writeopia.notemenu.data.model.NotesArrangement

data class NotesUi(
    val documentUiList: List<MenuItemUi>,
    val notesArrangement: NotesArrangement
)

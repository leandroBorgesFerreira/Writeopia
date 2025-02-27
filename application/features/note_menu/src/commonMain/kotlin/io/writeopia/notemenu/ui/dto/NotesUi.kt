package io.writeopia.notemenu.ui.dto

import io.writeopia.commonui.dtos.MenuItemUi
import io.writeopia.models.configuration.NotesArrangement

data class NotesUi(
    val documentUiList: List<MenuItemUi>,
    val notesArrangement: NotesArrangement
)

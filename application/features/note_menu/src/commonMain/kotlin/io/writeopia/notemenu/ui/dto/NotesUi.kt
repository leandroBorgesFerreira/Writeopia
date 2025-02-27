package io.writeopia.notemenu.ui.dto

import io.writeopia.commonui.dtos.MenuItemUi
import io.writeopia.core.configuration.models.NotesArrangement

data class NotesUi(
    val documentUiList: List<MenuItemUi>,
    val notesArrangement: NotesArrangement
)

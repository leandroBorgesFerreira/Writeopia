package io.writeopia.note_menu.ui.dto

import io.writeopia.sdk.models.story.StoryStep

sealed interface MenuItemUi {
    val documentId: String
    val title: String
    val selected: Boolean
    val isFavorite: Boolean

    data class DocumentUi(
        override val documentId: String,
        override val title: String,
        override val selected: Boolean,
        override val isFavorite: Boolean,
        val lastEdit: String,
        val parentId: String,
        val preview: List<StoryStep>
    ) : MenuItemUi

    data class FolderUi(
        override val documentId: String,
        override val title: String,
        override val selected: Boolean,
        override val isFavorite: Boolean,
        val itemsCount: Long,
        val parentId: String
    ) : MenuItemUi
}

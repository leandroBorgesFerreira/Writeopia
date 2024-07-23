package io.writeopia.note_menu.ui.dto

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.utils_module.node.Node

sealed interface MenuItemUi : Node<MenuItemUi> {
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
    ) : MenuItemUi {

        override val id: String = documentId

        override val acceptNodes: Boolean = false

        override fun addNotes(nodes: List<MenuItemUi>) {
            throw IllegalStateException(
                "A DocumentUi should not contain other documents. Use FolderUI"
            )
        }
    }

    data class FolderUi(
        override val documentId: String,
        override val title: String,
        override val selected: Boolean,
        override val isFavorite: Boolean,
        val itemsCount: Long,
        val parentId: String,
        val insideContent: MutableList<MenuItemUi> = mutableListOf()
    ) : MenuItemUi {

        override val id: String = documentId

        override val acceptNodes: Boolean = false

        override fun addNotes(nodes: List<MenuItemUi>) {
            insideContent.addAll(nodes)
        }
    }
}

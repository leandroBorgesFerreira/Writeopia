package io.writeopia.note_menu.ui.dto

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.utils.Traversable
import io.writeopia.utils_module.node.Node

sealed interface MenuItemUi : Node, Traversable {
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
        override val parentId: String,
        val preview: List<StoryStep>,
        override var depth: Int = 0
    ) : MenuItemUi {

        override val id: String = documentId

        override fun addNotes(nodes: List<Node>) {
            throw IllegalStateException(
                "A DocumentUi should not contain other documents. Use FolderUI"
            )
        }

        override fun getNodes(): List<Node> = emptyList()
    }

    data class FolderUi(
        override val documentId: String,
        override val title: String,
        override val selected: Boolean,
        override val isFavorite: Boolean,
        val itemsCount: Long,
        override val parentId: String,
        val expanded: Boolean,
        override var depth: Int = 0,
        val insideContent: MutableList<MenuItemUi> = mutableListOf()
    ) : MenuItemUi, Traversable {

        override val id: String = documentId

        override fun addNotes(nodes: List<Node>) {
            insideContent.addAll(nodes as List<MenuItemUi>)
        }

        override fun getNodes(): List<Node> = insideContent

        companion object {
            fun root() = FolderUi(
                documentId = Folder.ROOT_PATH,
                title = "",
                selected = false,
                isFavorite = false,
                itemsCount = 0,
                parentId = "",
                expanded = false,
                depth = 0
            )
        }
    }
}

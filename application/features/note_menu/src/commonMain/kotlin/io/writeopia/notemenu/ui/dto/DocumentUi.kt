package io.writeopia.notemenu.ui.dto

import io.writeopia.common.utils.Node
import io.writeopia.models.Folder
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.utils.Traversable

sealed interface MenuItemUi : Node, Traversable {
    val documentId: String
    val title: String
    val selected: Boolean
    val isFavorite: Boolean
    val highlighted: Boolean

    data class DocumentUi(
        override val documentId: String,
        override val title: String,
        override val selected: Boolean,
        override val isFavorite: Boolean,
        val lastEdit: String,
        override val parentId: String,
        val preview: List<StoryStep>,
        override var depth: Int = 0,
        override val highlighted: Boolean
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
        val insideContent: MutableList<MenuItemUi> = mutableListOf(),
        override val highlighted: Boolean
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
                depth = 0,
                highlighted = false
            )
        }
    }
}

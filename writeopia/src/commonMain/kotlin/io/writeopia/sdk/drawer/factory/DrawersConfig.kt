package io.writeopia.sdk.drawer.factory

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.text.edition.TextCommandHandler

data class DrawersConfig(
    val onTextEdit: (String, Int) -> Unit = { _, _ -> },
    val onTitleEdit: (String, Int) -> Unit = { _, _ -> },
    val onLineBreak: (Action.LineBreak) -> Unit = {},
    val mergeRequest: (Action.Merge) -> Unit = {},
    val moveRequest: (Action.Move) -> Unit = {},
    val checkRequest: (Action.StoryStateChange) -> Unit = {},
    val onDeleteRequest: (Action.DeleteStory) -> Unit = {},
    val changeStoryType: (Int, StoryType, CommandInfo?) -> Unit = { _, _, _ -> },
    val onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    val clickAtTheEnd: () -> Unit = {},
    val onHeaderClick: () -> Unit = {},
    val defaultBorder: @Composable () -> Shape = { MaterialTheme.shapes.medium },
    val groupsBackgroundColor: @Composable () -> Color = { Color.Transparent },
    val textCommandHandler: TextCommandHandler,
    val nextFocus: (Int) -> Unit = {},
) {

    companion object {
        @Composable
        fun fromManager(
            manager: WriteopiaManager,
            onHeaderClick: () -> Unit = {},
            groupsBackgroundColor: Color = Color.Transparent,
            defaultBorder: Shape = MaterialTheme.shapes.medium
        ): DrawersConfig =
            DrawersConfig(
                onTextEdit = manager::onTextEdit,
                onTitleEdit = manager::onTitleEdit,
                onLineBreak = manager::onLineBreak,
                mergeRequest = manager::mergeRequest,
                moveRequest = manager::moveRequest,
                checkRequest = manager::changeStoryState,
                onDeleteRequest = manager::onDelete,
                changeStoryType = manager::changeStoryType,
                nextFocus = manager::nextFocusOrCreate,
                clickAtTheEnd = manager::clickAtTheEnd,
                onHeaderClick = onHeaderClick,
                onSelected = manager::onSelected,
                groupsBackgroundColor = { groupsBackgroundColor },
                defaultBorder = { defaultBorder },
                textCommandHandler = TextCommandHandler.defaultCommands(manager)
            )
    }
}
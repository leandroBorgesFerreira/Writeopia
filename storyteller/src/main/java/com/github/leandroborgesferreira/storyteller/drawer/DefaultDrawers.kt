package com.github.leandroborgesferreira.storyteller.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storyteller.drawer.commands.CommandsDecoratorDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.AddButtonDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.CheckItemDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.HeaderDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.ImageGroupDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.ImageDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.ImageDrawer.Companion.defaultModifier
import com.github.leandroborgesferreira.storyteller.drawer.content.LargeEmptySpace
import com.github.leandroborgesferreira.storyteller.drawer.content.MessageDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.VideoDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.SpaceDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.TitleDrawer
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.text.edition.TextCommandHandler

object DefaultDrawers {

    fun create(
        editable: Boolean = false,
        manager: StoryTellerManager,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit,
        defaultBorder: Shape
    ): Map<String, StoryStepDrawer> =
        create(
            editable = editable,
            onTextEdit = manager::onTextEdit,
            onTitleEdit = manager::onTitleEdit,
            onLineBreak = manager::onLineBreak,
            mergeRequest = manager::mergeRequest,
            moveRequest = manager::moveRequest,
            checkRequest = manager::checkRequest,
            onDeleteRequest = manager::onDelete,
            createCheckItem = manager::createCheckItem,
            nextFocus = manager::nextFocusOrCreate,
            clickAtTheEnd = manager::clickAtTheEnd,
            onHeaderClick = onHeaderClick,
            onSelected = manager::onSelected,
            groupsBackgroundColor = groupsBackgroundColor,
            defaultBorder = defaultBorder
        )

    fun create(
        editable: Boolean = false,
        onTextEdit: (String, Int) -> Unit,
        onTitleEdit: (String, Int) -> Unit,
        onLineBreak: (Action.LineBreak) -> Unit,
        mergeRequest: (Action.Merge) -> Unit = { },
        moveRequest: (Action.Move) -> Unit = { },
        checkRequest: (Action.Check) -> Unit = { },
        onDeleteRequest: (Action.DeleteStory) -> Unit,
        createCheckItem: (Int) -> Unit,
        onSelected: (Boolean, Int) -> Unit,
        clickAtTheEnd: () -> Unit,
        onHeaderClick: () -> Unit,
        defaultBorder: Shape,
        groupsBackgroundColor: Color = Color.Transparent,
        nextFocus: (Int) -> Unit
    ): Map<String, StoryStepDrawer> =
        buildMap {
            val textCommandHandlerMessage = TextCommandHandler(
                mapOf(
                    "\n" to { storyStep, position ->
                        onLineBreak(Action.LineBreak(storyStep, position))
                    },
                    "-[]" to { _, position ->
                        createCheckItem(position)
                    }
                )
            )

            val textCommandHandlerCheckItem = TextCommandHandler(
                mapOf(
                    "\n" to { storyStep, position ->
                        onLineBreak(Action.LineBreak(storyStep, position))
                    },
                )
            )

            val commandsComposite: (StoryStepDrawer) -> StoryStepDrawer = { stepDrawer ->
                CommandsDecoratorDrawer(
                    stepDrawer,
                    onDelete = onDeleteRequest,
                )
            }

            val imageDrawer = ImageDrawer(
                containerModifier = { inBound -> Modifier.defaultModifier(inBound) },
                mergeRequest = mergeRequest
            )

            val imageDrawerInGroup = ImageDrawer(
                containerModifier = { inBound -> Modifier.defaultModifier(inBound) },
                mergeRequest = mergeRequest
            )

            val messageBoxDrawer = MessageDrawer(
                containerModifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(shape = defaultBorder)
                    .background(groupsBackgroundColor),
                innerContainerModifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                onTextEdit = onTextEdit,
                onDeleteRequest = onDeleteRequest,
                commandHandler = textCommandHandlerMessage,
                onSelected = onSelected,
            )

            val messageDrawer = MessageDrawer(
                containerModifier = Modifier,
                innerContainerModifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                onTextEdit = onTextEdit,
                onDeleteRequest = onDeleteRequest,
                commandHandler = textCommandHandlerMessage,
                onSelected = onSelected,
            )

            val checkItemDrawer = CheckItemDrawer(
                onCheckedChange = checkRequest,
                onTextEdit = onTextEdit,
                onDeleteRequest = onDeleteRequest,
                commandHandler = textCommandHandlerCheckItem,
                onSelected = onSelected,
            )

            val headerDrawer = HeaderDrawer(
                titleDrawer = {
                    TitleDrawer(
                        containerModifier = Modifier.align(Alignment.BottomStart),
                        onTextEdit = onTitleEdit,
                        onLineBreak = onLineBreak,
                    )
                },
                headerClick = onHeaderClick
            )

            put(StoryType.MESSAGE_BOX.type, messageBoxDrawer)
            put(StoryType.MESSAGE.type, messageDrawer)
            put(StoryType.ADD_BUTTON.type, AddButtonDrawer())
            put(StoryType.IMAGE.type, if (editable) commandsComposite(imageDrawer) else imageDrawer)
            put(
                StoryType.GROUP_IMAGE.type,
                if (editable) {
                    commandsComposite(ImageGroupDrawer(commandsComposite(imageDrawerInGroup)))
                } else {
                    ImageGroupDrawer(imageDrawerInGroup)
                }
            )
            put(
                StoryType.VIDEO.type,
                if (editable) commandsComposite(VideoDrawer()) else VideoDrawer()
            )

            put(StoryType.SPACE.type, SpaceDrawer(moveRequest))
            put(StoryType.LARGE_SPACE.type, LargeEmptySpace(moveRequest, clickAtTheEnd))
            put(StoryType.CHECK_ITEM.type, checkItemDrawer)
            put(StoryType.TITLE.type, headerDrawer)
        }
}

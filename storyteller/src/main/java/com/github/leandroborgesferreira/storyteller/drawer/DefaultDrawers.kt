package com.github.leandroborgesferreira.storyteller.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.leandroborgesferreira.storyteller.drawer.commands.CommandsDecoratorDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.AddButtonDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.CheckItemDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.HeaderDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.ImageGroupDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.ImageDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.LargeEmptySpace
import com.github.leandroborgesferreira.storyteller.drawer.content.SwipeMessageDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.VideoDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.SpaceDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.TitleDrawer
import com.github.leandroborgesferreira.storyteller.drawer.content.defaultImageShape
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.command.CommandFactory
import com.github.leandroborgesferreira.storyteller.model.command.CommandInfo
import com.github.leandroborgesferreira.storyteller.model.command.CommandTrigger
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.models.story.StoryType
import com.github.leandroborgesferreira.storyteller.text.edition.TextCommandHandler

object DefaultDrawers {

    fun create(
        editable: Boolean = false,
        manager: StoryTellerManager,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit,
        defaultBorder: Shape
    ): Map<Int, StoryStepDrawer> =
        create(
            editable = editable,
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
        checkRequest: (Action.StoryStateChange) -> Unit = { },
        onDeleteRequest: (Action.DeleteStory) -> Unit,
        changeStoryType: (Int, StoryType, CommandInfo) -> Unit,
        onSelected: (Boolean, Int) -> Unit,
        clickAtTheEnd: () -> Unit,
        onHeaderClick: () -> Unit,
        defaultBorder: Shape,
        groupsBackgroundColor: Color = Color.Transparent,
        nextFocus: (Int) -> Unit
    ): Map<Int, StoryStepDrawer> =
        buildMap {
            val textCommandHandlerMessage = TextCommandHandler(
                mapOf(
                    CommandFactory.lineBreak() to { storyStep, position ->
                        onLineBreak(Action.LineBreak(storyStep, position))
                    },
                    CommandFactory.checkItem() to { _, position ->
                        changeStoryType(
                            position,
                            StoryTypes.CHECK_ITEM.type,
                            CommandInfo(
                                CommandFactory.checkItem(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h1() to { _, position ->
                        changeStoryType(
                            position,
                            StoryTypes.H1.type,
                            CommandInfo(
                                CommandFactory.h1(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h2() to { _, position ->
                        changeStoryType(
                            position,
                            StoryTypes.H2.type,
                            CommandInfo(
                                CommandFactory.h2(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h3() to { _, position ->
                        changeStoryType(
                            position,
                            StoryTypes.H3.type,
                            CommandInfo(
                                CommandFactory.h3(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    },
                    CommandFactory.h4() to { _, position ->
                        changeStoryType(
                            position,
                            StoryTypes.H4.type,
                            CommandInfo(
                                CommandFactory.h4(),
                                CommandTrigger.WRITTEN
                            )
                        )
                    }
                )
            )

            val textCommandHandlerCheckItem = TextCommandHandler(
                mapOf(
                    CommandFactory.lineBreak() to { storyStep, position ->
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
                containerModifier = Modifier::defaultImageShape,
                mergeRequest = mergeRequest
            )

            val imageDrawerInGroup = ImageDrawer(
                containerModifier = Modifier::defaultImageShape,
                mergeRequest = mergeRequest
            )

            val messageBoxDrawer = SwipeMessageDrawer(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(shape = defaultBorder)
                    .background(groupsBackgroundColor),
                textModifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                onTextEdit = onTextEdit,
                onDeleteRequest = onDeleteRequest,
                commandHandler = textCommandHandlerMessage,
                onSelected = onSelected,
            )

            val swipeMessageDrawer = SwipeMessageDrawer(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
                textModifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                onTextEdit = onTextEdit,
                onDeleteRequest = onDeleteRequest,
                commandHandler = textCommandHandlerMessage,
                onSelected = onSelected,
            )

            val createHDrawer = { fontSize: TextUnit ->
                SwipeMessageDrawer(
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
                    textModifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                    onTextEdit = onTextEdit,
                    onDeleteRequest = onDeleteRequest,
                    commandHandler = textCommandHandlerMessage,
                    onSelected = onSelected,
                    textStyle = {
                        TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = fontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }

            val h1MessageDrawer = createHDrawer(28.sp)
            val h2MessageDrawer = createHDrawer(24.sp)
            val h3MessageDrawer = createHDrawer(20.sp)
            val h4MessageDrawer = createHDrawer(18.sp)

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

            put(StoryTypes.MESSAGE_BOX.type.number, messageBoxDrawer)
            put(StoryTypes.MESSAGE.type.number, swipeMessageDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(
                StoryTypes.IMAGE.type.number,
                if (editable) commandsComposite(imageDrawer) else imageDrawer
            )
            put(
                StoryTypes.GROUP_IMAGE.type.number,
                if (editable) {
                    commandsComposite(ImageGroupDrawer(commandsComposite(imageDrawerInGroup)))
                } else {
                    ImageGroupDrawer(imageDrawerInGroup)
                }
            )
            put(
                StoryTypes.VIDEO.type.number,
                if (editable) commandsComposite(VideoDrawer()) else VideoDrawer()
            )

            put(StoryTypes.SPACE.type.number, SpaceDrawer(moveRequest))
            put(StoryTypes.LARGE_SPACE.type.number, LargeEmptySpace(moveRequest, clickAtTheEnd))
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            put(StoryTypes.H1.type.number, h1MessageDrawer)
            put(StoryTypes.H2.type.number, h2MessageDrawer)
            put(StoryTypes.H3.type.number, h3MessageDrawer)
            put(StoryTypes.H4.type.number, h4MessageDrawer)
        }
}

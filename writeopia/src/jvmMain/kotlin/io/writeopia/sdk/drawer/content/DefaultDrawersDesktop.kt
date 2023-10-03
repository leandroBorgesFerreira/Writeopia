package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.command.CommandFactory
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.command.CommandTrigger
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.utils.ui.defaultTextStyle

object DefaultDrawersDesktop {

    @Composable
    fun create(
        manager: WriteopiaManager,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {}
    ): Map<Int, StoryStepDrawer> =
        create(
            onTextEdit = manager::onTextEdit,
            onTitleEdit = manager::onTitleEdit,
            onLineBreak = manager::onLineBreak,
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

    @Composable
    fun create(
        onTextEdit: (String, Int) -> Unit = { _, _ -> },
        onTitleEdit: (String, Int) -> Unit = { _, _ -> },
        onLineBreak: (Action.LineBreak) -> Unit = {},
        moveRequest: (Action.Move) -> Unit = {},
        checkRequest: (Action.StoryStateChange) -> Unit = {},
        onDeleteRequest: (Action.DeleteStory) -> Unit = {},
        changeStoryType: (Int, StoryType, CommandInfo?) -> Unit = { _, _, _ -> },
        onSelected: (Boolean, Int) -> Unit = { _, _ -> },
        clickAtTheEnd: () -> Unit = {},
        onHeaderClick: () -> Unit = {},
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        groupsBackgroundColor: Color = Color.Transparent,
        textCommandHandler: TextCommandHandler = TextCommandHandler(
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
                }, CommandFactory.unOrderedList() to { _, position ->
                    changeStoryType(
                        position,
                        StoryTypes.UNORDERED_LIST_ITEM.type,
                        CommandInfo(
                            CommandFactory.unOrderedList(),
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
        ),
        nextFocus: (Int) -> Unit = {}
    ): Map<Int, StoryStepDrawer> =
        buildMap {
            val androidMessageDrawer: RowScope.(FocusRequester) -> SimpleMessageDrawer = { focusRequester ->
                DesktopMessageDrawer(
                    focusRequester = focusRequester,
                    commandHandler = textCommandHandler,
                )
            }

            val messageBoxDrawer = SwipeMessageDrawer(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(shape = defaultBorder)
                    .background(groupsBackgroundColor),
                onSelected = onSelected,
                simpleMessageDrawer = androidMessageDrawer
            )

            val swipeMessageDrawer = SwipeMessageDrawer(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
                onSelected = onSelected,
                simpleMessageDrawer = androidMessageDrawer
            )

            val createHDrawer = { fontSize: TextUnit ->
                SwipeMessageDrawer(
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
                    onSelected = onSelected,
                    simpleMessageDrawer = { focusRequester ->
                        DesktopMessageDrawer(
                            textStyle = {
                                defaultTextStyle().copy(fontSize = fontSize)
                            },
                            focusRequester = focusRequester
                        )
                    }
                )
            }

            val h1MessageDrawer = createHDrawer(28.sp)
            val h2MessageDrawer = createHDrawer(24.sp)
            val h3MessageDrawer = createHDrawer(20.sp)
            val h4MessageDrawer = createHDrawer(18.sp)

            val checkItemDrawer = CheckItemDrawer(
                modifier = Modifier.padding(start = 18.dp, end = 12.dp),
                onCheckedChange = checkRequest,
                onTextEdit = onTextEdit,
                emptyErase = { position ->
                    changeStoryType(position, StoryTypes.MESSAGE.type, null)
                },
                commandHandler = textCommandHandler,
                onSelected = onSelected,
            )

            val unOrderedListItemDrawer =
                UnOrderedListItemDrawer(
                    modifier = Modifier.padding(start = 18.dp, end = 12.dp),
                    onSelected = onSelected,
                    messageDrawer = androidMessageDrawer
                )

            val headerDrawer = HeaderDrawer(
                drawer = {
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
            put(StoryTypes.SPACE.type.number, SpaceDrawer(moveRequest))
            put(StoryTypes.LARGE_SPACE.type.number, LargeEmptySpace(moveRequest, clickAtTheEnd))
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            put(StoryTypes.H1.type.number, h1MessageDrawer)
            put(StoryTypes.H2.type.number, h2MessageDrawer)
            put(StoryTypes.H3.type.number, h3MessageDrawer)
            put(StoryTypes.H4.type.number, h4MessageDrawer)
        }
}

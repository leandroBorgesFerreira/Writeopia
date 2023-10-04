package io.writeopia.sdk.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.drawer.commands.CommandsDecoratorDrawer
import io.writeopia.sdk.drawer.content.*
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.utils.ui.defaultTextStyle

object DefaultDrawersAndroid {

    @Composable
    fun create(
        manager: WriteopiaManager,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {}
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
            defaultBorder = defaultBorder,
            textCommandHandler = TextCommandHandler.defaultCommands(manager)
        )

    @Composable
    fun create(
        editable: Boolean = false,
        onTextEdit: (String, Int) -> Unit = { _, _ -> },
        onTitleEdit: (String, Int) -> Unit = { _, _ -> },
        onLineBreak: (Action.LineBreak) -> Unit = {},
        mergeRequest: (Action.Merge) -> Unit = {},
        moveRequest: (Action.Move) -> Unit = {},
        checkRequest: (Action.StoryStateChange) -> Unit = {},
        onDeleteRequest: (Action.DeleteStory) -> Unit = {},
        changeStoryType: (Int, StoryType, CommandInfo?) -> Unit = { _, _, _ -> },
        onSelected: (Boolean, Int) -> Unit = { _, _ -> },
        clickAtTheEnd: () -> Unit = {},
        onHeaderClick: () -> Unit = {},
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        groupsBackgroundColor: Color = Color.Transparent,
        textCommandHandler: TextCommandHandler,
        nextFocus: (Int) -> Unit = {}
    ): Map<Int, StoryStepDrawer> {
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

        val focusRequesterMessageBox = remember { FocusRequester() }
        val messageBoxDrawer = SwipeMessageDrawer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(shape = defaultBorder)
                .background(groupsBackgroundColor),
            focusRequester = focusRequesterMessageBox,
            onSelected = onSelected,
            simpleMessageDrawer = {
                AndroidMessageDrawer(
                    modifier = Modifier.weight(1F),
                    onTextEdit = onTextEdit,
                    focusRequester = focusRequesterMessageBox,
                    commandHandler = textCommandHandler,
                    onDeleteRequest = onDeleteRequest
                )
            }
        )

        val focusRequesterSwipeMessage = remember { FocusRequester() }
        val swipeMessageDrawer = SwipeMessageDrawer(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
            onSelected = onSelected,
            focusRequester = focusRequesterSwipeMessage,
            simpleMessageDrawer = {
                AndroidMessageDrawer(
                    modifier = Modifier.weight(1F),
                    onTextEdit = onTextEdit,
                    focusRequester = focusRequesterSwipeMessage,
                    commandHandler = textCommandHandler,
                    onDeleteRequest = onDeleteRequest
                )
            }
        )

        val createHDrawer = @Composable { fontSize: TextUnit ->
            val focusRequesterH = remember { FocusRequester() }
            SwipeMessageDrawer(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
                onSelected = onSelected,
                focusRequester = focusRequesterH,
                simpleMessageDrawer = {
                    AndroidMessageDrawer(
                        modifier = Modifier.weight(1F),
                        onTextEdit = onTextEdit,
                        textStyle = {
                            defaultTextStyle(it).copy(fontSize = fontSize)
                        },
                        focusRequester = focusRequesterH,
                        onDeleteRequest = onDeleteRequest
                    )
                }
            )
        }

        val h1MessageDrawer = createHDrawer(28.sp)
        val h2MessageDrawer = createHDrawer(24.sp)
        val h3MessageDrawer = createHDrawer(20.sp)
        val h4MessageDrawer = createHDrawer(18.sp)

        val focusRequesterCheckItem = remember { FocusRequester() }
        val checkItemDrawer = CheckItemDrawer(
            modifier = Modifier.padding(start = 18.dp, end = 12.dp),
            onCheckedChange = checkRequest,
            focusRequester = focusRequesterCheckItem,
            onSelected = onSelected,
            messageDrawer = {
                AndroidMessageDrawer(
                    modifier = Modifier.weight(1F),
                    onTextEdit = onTextEdit,
                    focusRequester = focusRequesterCheckItem,
                    commandHandler = textCommandHandler,
                    onDeleteRequest = onDeleteRequest,
                    emptyErase = { position ->
                        changeStoryType(position, StoryTypes.MESSAGE.type, null)
                    },
                )
            }
        )

        val focusRequesterUnOrderedList = remember { FocusRequester() }
        val unOrderedListItemDrawer =
            UnOrderedListItemDrawer(
                modifier = Modifier.padding(start = 18.dp, end = 12.dp),
                onSelected = onSelected,
                focusRequester = focusRequesterUnOrderedList,
                messageDrawer = {
                    AndroidMessageDrawer(
                        modifier = Modifier.weight(1F),
                        onTextEdit = onTextEdit,
                        focusRequester = focusRequesterUnOrderedList,
                        commandHandler = textCommandHandler,
                        onDeleteRequest = onDeleteRequest,
                        emptyErase = { position ->
                            changeStoryType(position, StoryTypes.MESSAGE.type, null)
                        },
                    )
                }
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

        return buildMap {
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
                    commandsComposite(RowGroupDrawer(commandsComposite(imageDrawerInGroup)))
                } else {
                    RowGroupDrawer(imageDrawerInGroup)
                }
            )
            put(
                StoryTypes.VIDEO.type.number,
                if (editable) commandsComposite(VideoDrawer()) else VideoDrawer()
            )
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
}

package io.writeopia.sdk.drawer.factory

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
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.commands.CommandsDecoratorDrawer
import io.writeopia.sdk.drawer.content.*
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryTypes
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
        create(DrawersConfig.fromManager(manager, onHeaderClick, groupsBackgroundColor, defaultBorder))

    @Composable
    fun create(drawersConfig: DrawersConfig): Map<Int, StoryStepDrawer> {
        val commandsComposite: (StoryStepDrawer) -> StoryStepDrawer = { stepDrawer ->
            CommandsDecoratorDrawer(
                stepDrawer,
                onDelete = drawersConfig.onDeleteRequest,
            )
        }

        val imageDrawer = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = drawersConfig.mergeRequest
        )

        val imageDrawerInGroup = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = drawersConfig.mergeRequest
        )

        val focusRequesterMessageBox = remember { FocusRequester() }
        val messageBoxDrawer = swipeMessageDrawer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(shape = drawersConfig.defaultBorder)
                .background(drawersConfig.groupsBackgroundColor),
            focusRequester = focusRequesterMessageBox,
            onSelected = drawersConfig.onSelected,
            messageDrawer = {
                AndroidMessageDrawer(
                    modifier = Modifier.weight(1F),
                    onTextEdit = drawersConfig.onTextEdit,
                    focusRequester = focusRequesterMessageBox,
                    commandHandler = drawersConfig.textCommandHandler,
                    onDeleteRequest = drawersConfig.onDeleteRequest
                )
            }
        )

        val focusRequesterSwipeMessage = remember { FocusRequester() }
        val swipeMessageDrawer = swipeMessageDrawer(
            modifier = Modifier.padding(horizontal = 12.dp),
            onSelected = drawersConfig.onSelected,
            focusRequester = focusRequesterSwipeMessage,
            messageDrawer = {
                AndroidMessageDrawer(
                    modifier = Modifier.weight(1F),
                    onTextEdit = drawersConfig.onTextEdit,
                    focusRequester = focusRequesterSwipeMessage,
                    commandHandler = drawersConfig.textCommandHandler,
                    onDeleteRequest = drawersConfig.onDeleteRequest
                )
            }
        )

        val createHDrawer = @Composable { fontSize: TextUnit ->
            val focusRequesterH = remember { FocusRequester() }
            swipeMessageDrawer(
                modifier = Modifier.padding(horizontal = 12.dp),
                onSelected = drawersConfig.onSelected,
                focusRequester = focusRequesterH,
                messageDrawer = {
                    AndroidMessageDrawer(
                        modifier = Modifier.weight(1F),
                        onTextEdit = drawersConfig.onTextEdit,
                        textStyle = {
                            defaultTextStyle(it).copy(fontSize = fontSize)
                        },
                        focusRequester = focusRequesterH,
                        commandHandler = drawersConfig.textCommandHandler,
                        onDeleteRequest = drawersConfig.onDeleteRequest
                    )
                }
            )
        }

        val h1MessageDrawer = createHDrawer(28.sp)
        val h2MessageDrawer = createHDrawer(24.sp)
        val h3MessageDrawer = createHDrawer(20.sp)
        val h4MessageDrawer = createHDrawer(18.sp)

        val focusRequesterCheckItem = remember { FocusRequester() }
        val checkItemDrawer = checkItemDrawer(
            modifier = Modifier.padding(start = 18.dp, end = 12.dp),
            onCheckedChange = drawersConfig.checkRequest,
            focusRequester = focusRequesterCheckItem,
            onSelected = drawersConfig.onSelected,
            messageDrawer = {
                AndroidMessageDrawer(
                    modifier = Modifier.weight(1F),
                    onTextEdit = drawersConfig.onTextEdit,
                    focusRequester = focusRequesterCheckItem,
                    commandHandler = drawersConfig.textCommandHandler,
                    onDeleteRequest = drawersConfig.onDeleteRequest,
                    emptyErase = { position ->
                        drawersConfig.changeStoryType(position, StoryTypes.MESSAGE.type, null)
                    },
                )
            }
        )

        val focusRequesterUnOrderedList = remember { FocusRequester() }
        val unOrderedListItemDrawer =
            unOrderedListItemDrawer(
                modifier = Modifier.padding(start = 18.dp, end = 12.dp),
                onSelected = drawersConfig.onSelected,
                focusRequester = focusRequesterUnOrderedList,
                messageDrawer = {
                    AndroidMessageDrawer(
                        modifier = Modifier.weight(1F),
                        onTextEdit = drawersConfig.onTextEdit,
                        focusRequester = focusRequesterUnOrderedList,
                        commandHandler = drawersConfig.textCommandHandler,
                        onDeleteRequest = drawersConfig.onDeleteRequest,
                        emptyErase = { position ->
                            drawersConfig.changeStoryType(position, StoryTypes.MESSAGE.type, null)
                        },
                    )
                }
            )

        val headerDrawer = HeaderDrawer(
            drawer = {
                TitleDrawer(
                    containerModifier = Modifier.align(Alignment.BottomStart),
                    onTextEdit = drawersConfig.onTitleEdit,
                    onLineBreak = drawersConfig.onLineBreak,
                )
            },
            headerClick = drawersConfig.onHeaderClick
        )

        return buildMap {
            put(StoryTypes.MESSAGE_BOX.type.number, messageBoxDrawer)
            put(StoryTypes.MESSAGE.type.number, swipeMessageDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(
                StoryTypes.IMAGE.type.number,
                imageDrawer
            )
            put(
                StoryTypes.GROUP_IMAGE.type.number,
                RowGroupDrawer(imageDrawerInGroup)
            )
            put(
                StoryTypes.VIDEO.type.number,
                VideoDrawer()
            )
            put(StoryTypes.SPACE.type.number, SpaceDrawer(drawersConfig.moveRequest))
            put(
                StoryTypes.LARGE_SPACE.type.number,
                LargeEmptySpace(drawersConfig.moveRequest, drawersConfig.clickAtTheEnd)
            )
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

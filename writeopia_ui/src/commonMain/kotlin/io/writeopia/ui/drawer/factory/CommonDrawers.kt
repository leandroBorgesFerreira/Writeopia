package io.writeopia.ui.drawer.factory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.edition.TextCommandHandler
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.content.AddButtonDrawer
import io.writeopia.ui.drawer.content.LastEmptySpace
import io.writeopia.ui.drawer.content.SpaceDrawer
import io.writeopia.ui.drawer.content.TextDrawer
import io.writeopia.ui.drawer.content.checkItemDrawer
import io.writeopia.ui.drawer.content.headerDrawer
import io.writeopia.ui.drawer.content.swipeTextDrawer
import io.writeopia.ui.drawer.content.unOrderedListItemDrawer
import io.writeopia.ui.model.EmptyErase
import io.writeopia.ui.utils.codeBlockStyle
import io.writeopia.ui.utils.defaultTextStyle

private const val LARGE_START_PADDING = 16
private const val MEDIUM_START_PADDING = 8
private const val SMALL_START_PADDING = 4

private const val DRAG_ICON_WIDTH = 24

object CommonDrawers {

    @Composable
    fun create(
        manager: WriteopiaStateManager,
        marginAtBottom: Dp,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {},
        textCommandHandler: TextCommandHandler = TextCommandHandler.defaultCommands(manager),
        dragIconWidth: Dp = DRAG_ICON_WIDTH.dp,
        //Todo: Remove isEmptyErase
        eventListener: (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase) -> Boolean
    ): Map<Int, StoryStepDrawer> {
        val textBoxDrawer = swipeTextDrawer(
            modifier = Modifier
                .padding(horizontal = LARGE_START_PADDING.dp)
                .clip(shape = defaultBorder)
                .background(groupsBackgroundColor),
            dragIconWidth = DRAG_ICON_WIDTH.dp,
            onSelected = manager::onSelected,
            messageDrawer = {
                messageDrawer(
                    manager = manager,
                    textCommandHandler = textCommandHandler,
                    eventListener = eventListener,
                    allowLineBreaks = true,
                    emptyErase = EmptyErase.CHANGE_TYPE,
                )
            }
        )

        val codeBlockDrawer = swipeTextDrawer(
            modifier = Modifier
                .padding(horizontal = LARGE_START_PADDING.dp)
                .background(Color.Gray),
            dragIconWidth = dragIconWidth,
            onSelected = manager::onSelected,
            messageDrawer = {
                messageDrawer(
                    manager = manager,
                    textCommandHandler = TextCommandHandler.noCommands(),
                    eventListener = eventListener,
                    textStyle = { codeBlockStyle() },
//                    isEmptyErase = isEmptyErase,
//                    deleteOnEmptyErase = true,
                    allowLineBreaks = true,
                    emptyErase = EmptyErase.CHANGE_TYPE,
                )
            }
        )

        val swipeTextDrawer = swipeTextDrawer(
            manager,
            modifier = Modifier.padding(start = MEDIUM_START_PADDING.dp),
            dragIconWidth = dragIconWidth
        ) {
            messageDrawer(
                manager = manager,
                textCommandHandler = textCommandHandler,
                eventListener = eventListener,
                emptyErase = EmptyErase.DELETE,
//                deleteOnEmptyErase = true,
            )
        }

        val hxDrawers =
            defaultHxDrawers(
                manager = manager,
                modifier = Modifier.padding(horizontal = SMALL_START_PADDING.dp),
                dragIconWidth = dragIconWidth
            ) { fontSize ->
                messageDrawer(
                    manager,
                    textCommandHandler = TextCommandHandler.noCommands(),
                    eventListener = eventListener,
                    emptyErase = EmptyErase.CHANGE_TYPE,
//                    isEmptyErase = isEmptyErase,
//                    deleteOnEmptyErase = false
                )
            }
        val checkItemDrawer = checkItemDrawer(
            manager,
            Modifier.padding(horizontal = LARGE_START_PADDING.dp),
            dragIconWidth = dragIconWidth,
        ) {
            messageDrawer(
                manager,
                textCommandHandler = TextCommandHandler.noCommands(),
                eventListener = eventListener,
                emptyErase = EmptyErase.CHANGE_TYPE,
//                deleteOnEmptyErase = false
            )
        }

        val unOrderedListItemDrawer =
            unOrderedListItemDrawer(
                manager,
                Modifier.padding(horizontal = LARGE_START_PADDING.dp),
                dragIconWidth = dragIconWidth,
            ) {
                messageDrawer(
                    manager,
                    textCommandHandler = TextCommandHandler.noCommands(),
                    eventListener = eventListener,
                    emptyErase = EmptyErase.CHANGE_TYPE,
//                    deleteOnEmptyErase = false
                )
            }
        val headerDrawer = headerDrawer(
            manager,
            headerClick = onHeaderClick,
            onKeyEvent = eventListener,
        )

        return buildMap {
            put(StoryTypes.TEXT_BOX.type.number, textBoxDrawer)
            put(StoryTypes.TEXT.type.number, swipeTextDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(StoryTypes.SPACE.type.number, SpaceDrawer(manager::moveRequest))
            put(
                StoryTypes.LAST_SPACE.type.number,
                LastEmptySpace(
                    moveRequest = manager::moveRequest,
                    click = manager::clickAtTheEnd,
                    height = marginAtBottom
                )
            )
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            put(StoryTypes.CODE_BLOCK.type.number, codeBlockDrawer)
            putAll(hxDrawers)
        }
    }

    @Composable
    private fun RowScope.messageDrawer(
        manager: WriteopiaStateManager,
        modifier: Modifier = Modifier,
        textStyle: @Composable (StoryStep) -> TextStyle = { defaultTextStyle(it) },
        textCommandHandler: TextCommandHandler = TextCommandHandler.defaultCommands(manager),
        allowLineBreaks: Boolean = false,
        emptyErase: EmptyErase,
        eventListener: (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase) -> Boolean
    ): TextDrawer {
        return TextDrawer(
            modifier = modifier.weight(1F),
            onKeyEvent = eventListener,
            onTextEdit = manager::changeStoryText,
            textStyle = textStyle,
            commandHandler = textCommandHandler,
            onLineBreak = manager::onLineBreak,
            allowLineBreaks = allowLineBreaks,
            emptyErase = emptyErase,
        )
    }
}
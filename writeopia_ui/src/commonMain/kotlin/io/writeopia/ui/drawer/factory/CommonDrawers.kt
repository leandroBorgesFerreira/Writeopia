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
import io.writeopia.ui.drawer.content.LastEmptySpace
import io.writeopia.ui.drawer.content.SpaceDrawer
import io.writeopia.ui.drawer.content.TextDrawer
import io.writeopia.ui.drawer.content.checkItemDrawer
import io.writeopia.ui.drawer.content.headerDrawer
import io.writeopia.ui.drawer.content.swipeTextDrawer
import io.writeopia.ui.drawer.content.unOrderedListItemDrawer
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
        isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },

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
                    isEmptyErase = isEmptyErase,
                    allowLineBreaks = true
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
                    modifier = Modifier.padding(vertical = 8.dp),
                    textStyle = { codeBlockStyle() },
                    isEmptyErase = isEmptyErase,
                    deleteOnEmptyErase = true,
                    allowLineBreaks = true
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
                isEmptyErase = isEmptyErase,
                deleteOnEmptyErase = true
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
                    textStyle = { defaultTextStyle(it).copy(fontSize = fontSize) },
                    isEmptyErase = isEmptyErase,
                    deleteOnEmptyErase = false
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
                isEmptyErase = isEmptyErase,
                deleteOnEmptyErase = false
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
                    isEmptyErase = isEmptyErase,
                    deleteOnEmptyErase = false
                )
            }
        val headerDrawer = headerDrawer(
            manager,
            headerClick = onHeaderClick,
            onKeyEvent = KeyEventListenerFactory.create(
                manager,
                isEmptyErase = { _, _ -> false },
                deleteOnEmptyErase = false,
            ),
        )

        return buildMap {
            put(StoryTypes.TEXT_BOX.type.number, textBoxDrawer)
            put(StoryTypes.TEXT.type.number, swipeTextDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, io.writeopia.ui.drawer.content.AddButtonDrawer())
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
        deleteOnEmptyErase: Boolean = false,
        allowLineBreaks: Boolean = false,
        isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
    ): TextDrawer {
        return TextDrawer(
            modifier = modifier.weight(1F),
            onKeyEvent =
            KeyEventListenerFactory.create(
                manager,
                isEmptyErase = isEmptyErase,
                deleteOnEmptyErase = deleteOnEmptyErase
            ),
            onTextEdit = manager::changeStoryText,
            textStyle = textStyle,
            commandHandler = textCommandHandler,
            onLineBreak = manager::onLineBreak,
            allowLineBreaks = allowLineBreaks
        )
    }
}
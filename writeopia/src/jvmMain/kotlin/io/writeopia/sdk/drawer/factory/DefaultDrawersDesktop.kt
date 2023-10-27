package io.writeopia.sdk.drawer.factory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.content.*
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.utils.ui.codeBlockStyle
import io.writeopia.sdk.utils.ui.defaultTextStyle
import java.awt.event.KeyEvent

private const val LARGE_START_PADDING = 24
private const val MEDIUM_START_PADDING = 12
private const val SMALL_START_PADDING = 4

object DefaultDrawersDesktop {

    @Composable
    fun create(
        manager: WriteopiaManager,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {}
    ): Map<Int, StoryStepDrawer> {
        val focusRequesterMessageBox = remember { FocusRequester() }
        val messageBoxDrawer = swipeMessageDrawer(
            modifier = Modifier
                .padding(horizontal = MEDIUM_START_PADDING.dp)
                .clip(shape = defaultBorder)
                .background(groupsBackgroundColor),
            focusRequester = focusRequesterMessageBox,
            onSelected = manager::onSelected,
            messageDrawer = {
                desktopMessageBoxDrawer(manager)
            }
        )

        val codeBlockShape = RoundedCornerShape(4.dp)
        val focusRequesterCodeBlock = remember { FocusRequester() }
        val codeBlockDrawer = swipeMessageDrawer(
            modifier = Modifier
                .clip(shape = codeBlockShape)
                .padding(LARGE_START_PADDING.dp)
                .background(Color.LightGray)
                .border(1.dp, color = Color.Gray, codeBlockShape),
            focusRequester = focusRequesterCodeBlock,
            onSelected = manager::onSelected,
            messageDrawer = {
                desktopMessageBoxDrawer(manager)
            }
        )

        val swipeMessageDrawer =
            swipeMessageDrawer(manager, modifier = Modifier.padding(start = MEDIUM_START_PADDING.dp)) {
                desktopMessageDrawer(manager, deleteOnEmptyErase = true)
            }
        val hxDrawers =
            defaultHxDrawers(manager, modifier = Modifier.padding(horizontal = SMALL_START_PADDING.dp)) { fontSize ->
                desktopMessageDrawer(manager, fontSize = fontSize, deleteOnEmptyErase = true)
            }
        val checkItemDrawer = checkItemDrawer(
            manager,
            modifier = Modifier.padding(start = LARGE_START_PADDING.dp)
        ) { desktopMessageDrawer(manager) }
        val unOrderedListItemDrawer =
            unOrderedListItemDrawer(
                manager,
                modifier = Modifier.padding(start = LARGE_START_PADDING.dp)
            ) { desktopMessageDrawer(manager) }
        val headerDrawer = headerDrawerDesktop(
            manager,
            headerClick = {},
            onKeyEvent = KeyEventListenerFactory.desktop(
                manager,
                isEmptyErase = { _, _ -> false },
                deleteOnEmptyErase = false
            ),
        )

        return buildMap {
            put(StoryTypes.TEXT_BOX.type.number, messageBoxDrawer)
            put(StoryTypes.TEXT.type.number, swipeMessageDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(StoryTypes.SPACE.type.number, SpaceDrawer(manager::moveRequest))
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            put(StoryTypes.CODE_BLOCK.type.number, codeBlockDrawer)
            put(
                StoryTypes.LAST_SPACE.type.number,
                LastEmptySpace(height = 30.dp, moveRequest = manager::moveRequest, click = manager::clickAtTheEnd)
            )
            putAll(hxDrawers)
        }
    }

    //Todo: Merge this with JS.
    @Composable
    private fun RowScope.desktopMessageDrawer(
        manager: WriteopiaManager,
        modifier: Modifier = Modifier,
        fontSize: TextUnit = 16.sp,
        textCommandHandler: TextCommandHandler = TextCommandHandler.defaultCommands(manager),
        deleteOnEmptyErase: Boolean = false,
    ): DesktopMessageDrawer {
        val focusRequester = remember { FocusRequester() }
        return DesktopMessageDrawer(
            modifier = modifier.weight(1F),
            onKeyEvent = KeyEventListenerFactoryDesktop.createDesktop(manager, deleteOnEmptyErase),
            onTextEdit = manager::changeStoryState,
            textStyle = { defaultTextStyle(it).copy(fontSize = fontSize) },
            focusRequester = focusRequester,
            commandHandler = textCommandHandler,
            onLineBreak = manager::onLineBreak,
        )
    }

    @Composable
    private fun RowScope.desktopMessageBoxDrawer(
        manager: WriteopiaManager,
        fontSize: TextUnit = 16.sp,
        textCommandHandler: TextCommandHandler = TextCommandHandler.defaultCommands(manager),
        deleteOnEmptyErase: Boolean = false,
    ): DesktopMessageDrawer {
        val focusRequester = remember { FocusRequester() }
        return DesktopMessageDrawer(
            modifier = Modifier.weight(1F).padding(8.dp),
            onKeyEvent = KeyEventListenerFactory.desktop(
                manager,
                isEmptyErase = { keyEvent, inputText ->
                    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE &&
                            inputText.selection.start == 0
                },
                deleteOnEmptyErase = deleteOnEmptyErase
            ),
            onTextEdit = manager::changeStoryState,
            textStyle = { codeBlockStyle() },
            focusRequester = focusRequester,
            commandHandler = textCommandHandler,
            onLineBreak = manager::onLineBreak,
            allowLineBreaks = true
        )
    }
}

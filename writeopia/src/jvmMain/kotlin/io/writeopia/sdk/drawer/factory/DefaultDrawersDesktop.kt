package io.writeopia.sdk.drawer.factory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
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
import io.writeopia.sdk.utils.ui.defaultTextStyle
import java.awt.event.KeyEvent

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
                .padding(horizontal = 16.dp)
                .clip(shape = defaultBorder)
                .background(groupsBackgroundColor),
            focusRequester = focusRequesterMessageBox,
            onSelected = manager::onSelected,
            messageDrawer = {
                desktopMessageBoxDrawer(manager)
            }
        )

        val focusRequesterCodeBlock = remember { FocusRequester() }
        val codeBlockDrawer = swipeMessageDrawer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(shape = defaultBorder)
                .background(Color.Gray),
            focusRequester = focusRequesterCodeBlock,
            onSelected = manager::onSelected,
            messageDrawer = {
                desktopMessageBoxDrawer(manager)
            }
        )

        val swipeMessageDrawer = swipeMessageDrawer(manager) {
            desktopMessageDrawer(manager, deleteOnEmptyErase = true)
        }
        val hxDrawers = defaultHxDrawers(manager) { fontSize ->
            desktopMessageDrawer(manager, fontSize, deleteOnEmptyErase = true)
        }
        val checkItemDrawer = checkItemDrawer(manager) { desktopMessageDrawer(manager) }
        val unOrderedListItemDrawer =
            unOrderedListItemDrawer(manager) { desktopMessageDrawer(manager) }
        val headerDrawer = headerDrawerDesktop(
            manager,
            headerClick = {},
            onKeyEvent = KeyEventListenerFactory.create(
                manager,
                isLineBreakKey = { keyEvent ->
                    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_ENTER
                },
                isEmptyErase = { _, _ -> false },
                deleteOnEmptyErase = false
            )
        )

        return buildMap {
            put(StoryTypes.MESSAGE_BOX.type.number, messageBoxDrawer)
            put(StoryTypes.MESSAGE.type.number, swipeMessageDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(StoryTypes.SPACE.type.number, SpaceDrawer(manager::moveRequest))
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            put(StoryTypes.CODE_BLOCK.type.number, codeBlockDrawer)
            putAll(hxDrawers)
        }
    }

    //Todo: Merge this with JS.
    @Composable
    private fun RowScope.desktopMessageDrawer(
        manager: WriteopiaManager,
        fontSize: TextUnit = 16.sp,
        textCommandHandler: TextCommandHandler = TextCommandHandler.defaultCommands(manager),
        deleteOnEmptyErase: Boolean = false,
    ): DesktopMessageDrawer {
        val focusRequester = remember { FocusRequester() }
        return DesktopMessageDrawer(
            modifier = Modifier.weight(1F),
            onKeyEvent = KeyEventListenerFactory.create(
                manager,
                isLineBreakKey = { keyEvent ->
                    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_ENTER
                },
                isEmptyErase = { keyEvent, inputText ->
                    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE && inputText.selection.start == 0
                },
                deleteOnEmptyErase = deleteOnEmptyErase
            ),
            onTextEdit = manager::onTextEdit,
            textStyle = { defaultTextStyle(it).copy(fontSize = fontSize) },
            focusRequester = focusRequester,
            commandHandler = textCommandHandler,
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
            modifier = Modifier.weight(1F),
            onKeyEvent = KeyEventListenerFactory.create(
                manager,
                isLineBreakKey = { false },
                isEmptyErase = { keyEvent, inputText ->
                    keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE && inputText.selection.start == 0
                },
                deleteOnEmptyErase = deleteOnEmptyErase
            ),
            onTextEdit = manager::onTextEdit,
            textStyle = { defaultTextStyle(it).copy(fontSize = fontSize) },
            focusRequester = focusRequester,
            commandHandler = textCommandHandler,
            allowLineBreaks = true
        )
    }
}

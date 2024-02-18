package io.writeopia.ui.drawer.factory

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.content.HeaderDrawer
import io.writeopia.ui.edition.TextCommandHandler
import org.jetbrains.skiko.SkikoKey
import io.writeopia.ui.drawer.content.JsTextDrawer
import io.writeopia.ui.drawer.content.SpaceDrawer
import io.writeopia.ui.drawer.content.js.DesktopTitleDrawer
import io.writeopia.ui.drawer.content.swipeTextDrawer

private const val LARGE_START_PADDING = 26
private const val MEDIUM_START_PADDING = 12
private const val SMALL_START_PADDING = 4

object DefaultDrawersJs {

    @Composable
    fun create(
        manager: WriteopiaStateManager,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {}
    ): Map<Int, StoryStepDrawer> {
        val focusRequesterMessageBoxSwipe = remember { FocusRequester() }
        val swipeMessageDrawer = swipeTextDrawer(
            modifier = Modifier.padding(start = MEDIUM_START_PADDING.dp),
//            focusRequester = focusRequesterMessageBoxSwipe
        ) {
            jsMessageDrawer(manager, deleteOnEmptyErase = true)
        }
        val hxDrawers =
            DefaultDrawersJs.defaultHxDrawers(
                manager,
//                modifier = Modifier.padding(horizontal = SMALL_START_PADDING.dp)
            ) { fontSize ->
                jsMessageDrawer(manager, fontSize = fontSize, deleteOnEmptyErase = true)
            }
//        val checkItemDrawer = checkItemDrawer(manager) { jsMessageDrawer(manager) }
//        val headerDrawer = headerDrawer(manager)
//        val unOrderedListItemDrawer = unOrderedListItemDrawer(manager) { jsMessageDrawer(manager) }

        return buildMap {
            put(StoryTypes.TEXT.type.number, swipeMessageDrawer)
            put(StoryTypes.SPACE.type.number, SpaceDrawer(manager::moveRequest))
//            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
//            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
//            put(StoryTypes.TITLE.type.number, headerDrawer)
//            put(
//                StoryTypes.LAST_SPACE.type.number,
//                LastEmptySpace(height = 30.dp, moveRequest = manager::moveRequest, click = manager::clickAtTheEnd)
//            )
            putAll(hxDrawers)
        }
    }

    @Composable
    fun RowScope.jsMessageDrawer(
        manager: WriteopiaStateManager,
        fontSize: TextUnit = 16.sp,
        deleteOnEmptyErase: Boolean = false
    ): JsTextDrawer {
        val focusRequester = remember { FocusRequester() }
        return JsTextDrawer(
            modifier = Modifier.weight(1F).padding(start = 8.dp),
            textStyle = TextStyle(fontSize = fontSize),
            onKeyEvent = KeyEventListenerFactory.js(
                manager,
                deleteOnEmptyErase = deleteOnEmptyErase,
                isLineBreakKey = { keyEvent -> keyEvent.nativeKeyEvent.key == SkikoKey.KEY_ENTER },
                isEmptyErase = { keyEvent, inputText ->
                    keyEvent.nativeKeyEvent.key == SkikoKey.KEY_BACKSPACE && inputText.selection.start == 0
                },
            ),
            onTextEdit = manager::changeStoryState,
            focusRequester = focusRequester,
            commandHandler = TextCommandHandler.defaultCommands(manager),
        )
    }

    @Composable
    fun defaultHxDrawers(
        writeopiaManager: WriteopiaStateManager,
        messageDrawer: @Composable RowScope.(TextUnit) -> SimpleTextDrawer
    ): Map<Int, StoryStepDrawer> {
        val createHDrawer = @Composable { fontSize: TextUnit ->
            val focusRequesterH = remember { FocusRequester() }
            swipeTextDrawer(
                modifier = Modifier.padding(horizontal = 12.dp),
                onSelected = writeopiaManager::onSelected,
//                focusRequester = focusRequesterH,
                messageDrawer = {
                    messageDrawer(fontSize)
                }
            )
        }

        val h1MessageDrawer = createHDrawer(28.sp)
        val h2MessageDrawer = createHDrawer(24.sp)
        val h3MessageDrawer = createHDrawer(20.sp)
        val h4MessageDrawer = createHDrawer(18.sp)

        return mapOf(
            StoryTypes.H1.type.number to h1MessageDrawer,
            StoryTypes.H2.type.number to h2MessageDrawer,
            StoryTypes.H3.type.number to h3MessageDrawer,
            StoryTypes.H4.type.number to h4MessageDrawer,
        )
    }

//    private fun headerDrawer(writeopiaManager: WriteopiaStateManager): StoryStepDrawer {
//        val onKeyEvent = KeyEventListenerFactory.js(
//            writeopiaManager,
//            deleteOnEmptyErase = false,
//            isEmptyErase = { _, _ -> false},
//            isLineBreakKey = { keyEvent -> keyEvent.nativeKeyEvent.key == SkikoKey.KEY_ENTER },
//        )
//
//        return HeaderDrawer(
//            drawer = {
//                DesktopTitleDrawer(
//                    modifier = Modifier.align(Alignment.BottomStart),
//                    onTextEdit = writeopiaManager::changeStoryState,
//                    onKeyEvent = onKeyEvent,
//                )
//            },
//        )
//    }

//    @Composable
//    fun checkItemDrawer(
//        writeopiaManager: WriteopiaStateManager,
//        messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
//    ): StoryStepDrawer {
//        val focusRequesterCheckItem = remember { FocusRequester() }
//        return checkItemDrawer(
//            modifier = Modifier.padding(start = LARGE_START_PADDING.dp, end = 12.dp),
//            onCheckedChange = writeopiaManager::changeStoryState,
//            onSelected = writeopiaManager::onSelected,
//            customBackgroundColor = Color.Transparent,
//            focusRequester = focusRequesterCheckItem,
//            messageDrawer = {
//                messageDrawer()
//            },
//        )
//    }
//
//    @Composable
//    fun unOrderedListItemDrawer(
//        writeopiaManager: WriteopiaStateManager,
//        messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
//    ): StoryStepDrawer {
//        val focusRequesterUnOrderedList = remember { FocusRequester() }
//
//        return unOrderedListItemDrawer(
//            modifier = Modifier.padding(start = LARGE_START_PADDING.dp, end = 12.dp),
//            onSelected = writeopiaManager::onSelected,
//            focusRequester = focusRequesterUnOrderedList,
//            customBackgroundColor = Color.Transparent,
//            messageDrawer = messageDrawer,
//        )
//    }
}

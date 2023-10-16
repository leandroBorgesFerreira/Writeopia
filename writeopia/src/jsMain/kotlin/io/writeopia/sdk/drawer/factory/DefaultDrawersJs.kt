package io.writeopia.sdk.drawer.factory

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
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.content.*
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.text.edition.TextCommandHandler
import org.jetbrains.skiko.SkikoKey

object DefaultDrawersJs {

    @Composable
    fun create(
        manager: WriteopiaManager,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {}
    ): Map<Int, StoryStepDrawer> {
        val focusRequesterMessageBoxSwipe = remember { FocusRequester() }
        val swipeMessageDrawer = swipeMessageDrawer(focusRequester = focusRequesterMessageBoxSwipe) {
            jsMessageDrawer(manager, deleteOnEmptyErase = true)
        }
        val hxDrawers = defaultHxDrawers(manager) { fontSize ->
            jsMessageDrawer(manager, fontSize = fontSize, deleteOnEmptyErase = true)
        }
        val checkItemDrawer = checkItemDrawer(manager) { jsMessageDrawer(manager) }
        val headerDrawer = headerDrawer(manager)
        val unOrderedListItemDrawer = unOrderedListItemDrawer(manager) { jsMessageDrawer(manager) }

        return buildMap {
            put(StoryTypes.TEXT.type.number, swipeMessageDrawer)
            put(StoryTypes.SPACE.type.number, SpaceDrawer(manager::moveRequest))
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            put(
                StoryTypes.LAST_SPACE.type.number,
                LastEmptySpace(height = 30.dp, moveRequest = manager::moveRequest, click = manager::clickAtTheEnd)
            )
            putAll(hxDrawers)
        }
    }

    @Composable
    fun RowScope.jsMessageDrawer(
        manager: WriteopiaManager,
        fontSize: TextUnit = 16.sp,
        deleteOnEmptyErase: Boolean = false
    ): JsMessageDrawer {
        val focusRequester = remember { FocusRequester() }
        return JsMessageDrawer(
            modifier = Modifier.weight(1F),
            textStyle = TextStyle(fontSize = fontSize),
            onKeyEvent = KeyEventListenerFactoryWeb.createWeb(
                manager,
                deleteOnEmptyErase = deleteOnEmptyErase
            ),
            onTextEdit = manager::changeStoryState,
            focusRequester = focusRequester,
            commandHandler = TextCommandHandler.defaultCommands(manager),
        )
    }

    @Composable
    fun defaultHxDrawers(
        writeopiaManager: WriteopiaManager,
        messageDrawer: @Composable RowScope.(TextUnit) -> SimpleMessageDrawer
    ): Map<Int, StoryStepDrawer> {
        val createHDrawer = @Composable { fontSize: TextUnit ->
            val focusRequesterH = remember { FocusRequester() }
            swipeMessageDrawer(
                modifier = Modifier.padding(horizontal = 12.dp),
                onSelected = writeopiaManager::onSelected,
                focusRequester = focusRequesterH,
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

    private fun headerDrawer(writeopiaManager: WriteopiaManager): StoryStepDrawer {
        val keyEvent = KeyEventListenerFactory.create(
            writeopiaManager,
            isLineBreakKey = { keyEvent -> keyEvent.nativeKeyEvent.key == SkikoKey.KEY_ENTER },
            deleteOnEmptyErase = false
        )

        return HeaderDrawer(
            drawer = {
                DesktopTitleDrawer(
                    modifier = Modifier.align(Alignment.BottomStart),
                    onTextEdit = writeopiaManager::changeStoryState,
                    onKeyEvent = keyEvent,
                )
            },
        )
    }

    @Composable
    fun checkItemDrawer(
        writeopiaManager: WriteopiaManager,
        messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
    ): StoryStepDrawer {
        val focusRequesterCheckItem = remember { FocusRequester() }
        return checkItemDrawer(
            modifier = Modifier.padding(start = 18.dp, end = 12.dp),
            onCheckedChange = writeopiaManager::changeStoryState,
            onSelected = writeopiaManager::onSelected,
            customBackgroundColor = Color.Transparent,
            focusRequester = focusRequesterCheckItem,
            messageDrawer = {
                messageDrawer()
            },
        )
    }

    @Composable
    fun unOrderedListItemDrawer(
        writeopiaManager: WriteopiaManager,
        messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
    ): StoryStepDrawer {
        val focusRequesterUnOrderedList = remember { FocusRequester() }

        return unOrderedListItemDrawer(
            modifier = Modifier.padding(start = 18.dp, end = 12.dp),
            onSelected = writeopiaManager::onSelected,
            focusRequester = focusRequesterUnOrderedList,
            customBackgroundColor = Color.Transparent,
            messageDrawer = messageDrawer,
        )
    }


}

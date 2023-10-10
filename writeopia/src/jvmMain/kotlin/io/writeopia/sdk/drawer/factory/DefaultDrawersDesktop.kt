package io.writeopia.sdk.drawer.factory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
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
import io.writeopia.sdk.drawer.content.*
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryTypes
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
            DrawersConfig.fromManager(manager, onHeaderClick, groupsBackgroundColor, defaultBorder)
        )

    @Composable
    fun create(drawersConfig: DrawersConfig): Map<Int, StoryStepDrawer> {
        val focusRequesterMessageBox = remember { FocusRequester() }
        val messageBoxDrawer = swipeMessageDrawer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(shape = drawersConfig.defaultBorder())
                .background(drawersConfig.groupsBackgroundColor()),
            focusRequester = focusRequesterMessageBox,
            onSelected = drawersConfig.onSelected,
            messageDrawer = {
                desktopMessageDrawer(drawersConfig)
            }
        )

        val swipeMessageDrawer = swipeMessageDrawer(drawersConfig) {
            desktopMessageDrawer(drawersConfig, emptyErase = null)
        }
        val hxDrawers = defaultHxDrawers(drawersConfig) { fontSize ->
            desktopMessageDrawer(drawersConfig, fontSize)
        }
        val checkItemDrawer = checkItemDrawer(drawersConfig) { desktopMessageDrawer(drawersConfig) }
        val unOrderedListItemDrawer = unOrderedListItemDrawer(drawersConfig) { desktopMessageDrawer(drawersConfig) }
        val headerDrawer = headerDrawer(drawersConfig)

        return buildMap {
            put(StoryTypes.MESSAGE_BOX.type.number, messageBoxDrawer)
            put(StoryTypes.MESSAGE.type.number, swipeMessageDrawer)
            put(StoryTypes.ADD_BUTTON.type.number, AddButtonDrawer())
            put(StoryTypes.SPACE.type.number, SpaceDrawer(drawersConfig.moveRequest))
//            put(
//                StoryTypes.LARGE_SPACE.type.number,
//                LargeEmptySpace(drawersConfig.moveRequest, drawersConfig.clickAtTheEnd)
//            )
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            putAll(hxDrawers)
        }
    }

    @Composable
    private fun RowScope.desktopMessageDrawer(
        drawersConfig: DrawersConfig,
        fontSize: TextUnit = 16.sp,
        emptyErase: ((Int) -> Unit)? = { position ->
            drawersConfig.changeStoryType(position, StoryTypes.MESSAGE.type, null)
        },
    ): DesktopMessageDrawer {
        val focusRequesterH = remember { FocusRequester() }
        return DesktopMessageDrawer(
            modifier = Modifier.weight(1F),
            onTextEdit = drawersConfig.onTextEdit,
            textStyle = { defaultTextStyle(it).copy(fontSize = fontSize) },
            focusRequester = focusRequesterH,
            commandHandler = drawersConfig.textCommandHandler,
            emptyErase = emptyErase,
            onDeleteRequest = drawersConfig.onDeleteRequest
        )
    }
}

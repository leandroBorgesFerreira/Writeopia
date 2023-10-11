package io.writeopia.sdk.drawer.factory

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
                .clip(shape = drawersConfig.defaultBorder())
                .background(drawersConfig.groupsBackgroundColor()),
            focusRequester = focusRequesterMessageBox,
            onSelected = drawersConfig.onSelected,
            messageDrawer = {
                androidMessageDrawer(drawersConfig, emptyErase = null)
            }
        )

        val swipeMessageDrawer = swipeMessageDrawer(drawersConfig) {
            androidMessageDrawer(drawersConfig, emptyErase = null)
        }
        val hxDrawers = defaultHxDrawers(drawersConfig) { fontSize ->
            androidMessageDrawer(drawersConfig, fontSize)
        }
        val checkItemDrawer = checkItemDrawer(drawersConfig) { androidMessageDrawer(drawersConfig) }
        val unOrderedListItemDrawer = unOrderedListItemDrawer(drawersConfig) { androidMessageDrawer(drawersConfig) }
        val headerDrawer = headerDrawer(drawersConfig)

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
            putAll(hxDrawers)
        }
    }


    @Composable
    private fun RowScope.androidMessageDrawer(
        drawersConfig: DrawersConfig,
        fontSize: TextUnit = 16.sp,
        emptyErase: ((Int) -> Unit)? = { position ->
            drawersConfig.changeStoryType(position, StoryTypes.MESSAGE.type, null)
        },
    ): MobileMessageDrawer {
        val focusRequester = remember { FocusRequester() }
        return MobileMessageDrawer(
            modifier = Modifier.weight(1F),
            isEmptyErase = { keyEvent, selection ->
                keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && selection.selection.start == 0
            },
            onTextEdit = drawersConfig.onTextEdit,
            textStyle = { defaultTextStyle(it).copy(fontSize = fontSize) },
            focusRequester = focusRequester,
            commandHandler = drawersConfig.textCommandHandler,
            emptyErase = emptyErase,
            onDeleteRequest = drawersConfig.onDeleteRequest
        )
    }
}

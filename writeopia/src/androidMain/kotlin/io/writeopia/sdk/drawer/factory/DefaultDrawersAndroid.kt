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
import io.writeopia.sdk.text.edition.TextCommandHandler
import io.writeopia.sdk.utils.ui.defaultTextStyle

object DefaultDrawersAndroid {

    @Composable
    fun create(
        manager: WriteopiaManager, defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {},
        textCommandHandler: TextCommandHandler = TextCommandHandler.defaultCommands(manager)
    ): Map<Int, StoryStepDrawer> {
        val commandsComposite: (StoryStepDrawer) -> StoryStepDrawer = { stepDrawer ->
            CommandsDecoratorDrawer(
                stepDrawer,
                onDelete = manager::onDelete,
            )
        }

        val imageDrawer = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest
        )

        val imageDrawerInGroup = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest
        )

        val focusRequesterMessageBox = remember { FocusRequester() }
        val messageBoxDrawer = swipeMessageDrawer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(shape = defaultBorder)
                .background(groupsBackgroundColor),
            focusRequester = focusRequesterMessageBox,
            onSelected = manager::onSelected,
            messageDrawer = {
                androidMessageDrawer(
                    manager,
                    emptyErase = null,
                    textCommandHandler = textCommandHandler
                )
            }
        )

        val swipeMessageDrawer = swipeMessageDrawer(manager) {
            androidMessageDrawer(
                manager,
                emptyErase = null,
                textCommandHandler = textCommandHandler
            )
        }
        val hxDrawers = defaultHxDrawers(manager) { fontSize ->
            androidMessageDrawer(manager, fontSize, textCommandHandler = textCommandHandler)
        }
        val checkItemDrawer = checkItemDrawer(manager) {
            androidMessageDrawer(
                manager,
                textCommandHandler = textCommandHandler
            )
        }
        val unOrderedListItemDrawer =
            unOrderedListItemDrawer(manager) {
                androidMessageDrawer(
                    manager,
                    textCommandHandler = textCommandHandler
                )
            }
        val headerDrawer = headerDrawer(manager, onHeaderClick)

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
            put(StoryTypes.SPACE.type.number, SpaceDrawer(manager::moveRequest))
            put(
                StoryTypes.LARGE_SPACE.type.number,
                LargeEmptySpace(manager::moveRequest, manager::clickAtTheEnd)
            )
            put(StoryTypes.CHECK_ITEM.type.number, checkItemDrawer)
            put(StoryTypes.UNORDERED_LIST_ITEM.type.number, unOrderedListItemDrawer)
            put(StoryTypes.TITLE.type.number, headerDrawer)
            putAll(hxDrawers)
        }
    }


    @Composable
    private fun RowScope.androidMessageDrawer(
        manager: WriteopiaManager,
        fontSize: TextUnit = 16.sp,
        emptyErase: ((Int) -> Unit)? = { position ->
            manager.changeStoryType(position, StoryTypes.MESSAGE.type, null)
        },
        textCommandHandler: TextCommandHandler
    ): MobileMessageDrawer {
        val focusRequester = remember { FocusRequester() }
        return MobileMessageDrawer(
            modifier = Modifier.weight(1F),
            isEmptyErase = { keyEvent, selection ->
                keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && selection.selection.start == 0
            },
            onTextEdit = manager::onTextEdit,
            textStyle = { defaultTextStyle(it).copy(fontSize = fontSize) },
            focusRequester = focusRequester,
            commandHandler = textCommandHandler,
            emptyErase = emptyErase,
            onDeleteRequest = manager::onDelete
        )
    }
}

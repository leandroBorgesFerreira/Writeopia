package io.writeopia.sdk.drawer.factory

import android.view.KeyEvent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.content.*
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.text.edition.TextCommandHandler

object DefaultDrawersAndroid {

    @Composable
    fun create(
        manager: WriteopiaManager, defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {},
        textCommandHandler: TextCommandHandler = TextCommandHandler.defaultCommands(manager)
    ): Map<Int, StoryStepDrawer> {
        val imageDrawer = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest
        )

        val imageDrawerInGroup = ImageDrawer(
            containerModifier = Modifier::defaultImageShape,
            mergeRequest = manager::mergeRequest
        )

        val commonDrawers = CommonDrawers.create(
            manager,
            marginAtBottom = 500.dp,
            defaultBorder,
            editable,
            groupsBackgroundColor,
            onHeaderClick,
            textCommandHandler,
            isEmptyErase = { keyEvent, inputText ->
                keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && inputText.selection.start == 0
            })

        return mapOf(
            StoryTypes.VIDEO.type.number to VideoDrawer(),
            StoryTypes.IMAGE.type.number to imageDrawer,
            StoryTypes.GROUP_IMAGE.type.number to RowGroupDrawer(imageDrawerInGroup)
        ) + commonDrawers
    }
}

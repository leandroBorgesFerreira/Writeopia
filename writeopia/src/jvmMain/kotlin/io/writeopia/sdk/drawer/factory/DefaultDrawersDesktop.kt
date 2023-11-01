package io.writeopia.sdk.drawer.factory

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import java.awt.event.KeyEvent

object DefaultDrawersDesktop {

    @Composable
    fun create(
        manager: WriteopiaManager,
        defaultBorder: Shape = MaterialTheme.shapes.medium,
        editable: Boolean = false,
        groupsBackgroundColor: Color = Color.Transparent,
        onHeaderClick: () -> Unit = {}
    ): Map<Int, StoryStepDrawer> =
        CommonDrawers.create(
            manager,
            30.dp,
            defaultBorder,
            editable,
            groupsBackgroundColor,
            onHeaderClick,
            isEmptyErase = { keyEvent, inputText ->
                keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE && inputText.selection.start == 0
            })
}

package io.writeopia.ui.drawer.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.edition.TextCommandHandler
import io.writeopia.ui.drawer.StoryStepDrawer
import java.awt.event.KeyEvent

object DefaultDrawersDesktop : DrawersFactory {

    @Composable
    override fun create(
        manager: WriteopiaStateManager,
        defaultBorder: Shape,
        editable: Boolean,
        groupsBackgroundColor: Color,
        onHeaderClick: () -> Unit,
        textCommandHandler: TextCommandHandler
    ): Map<Int, StoryStepDrawer> =
        CommonDrawers.create(
            manager,
            30.dp,
            defaultBorder,
            editable,
            groupsBackgroundColor,
            onHeaderClick,
            dragIconWidth = 16.dp,
            isEmptyErase = { keyEvent, inputText ->
                keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE && inputText.selection.start == 0
            })
}

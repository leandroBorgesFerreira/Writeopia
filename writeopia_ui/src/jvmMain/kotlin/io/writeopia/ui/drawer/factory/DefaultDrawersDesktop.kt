package io.writeopia.ui.drawer.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.TextFieldValue
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
            lineBreakByContent = true,
            eventListener = KeyEventListenerFactory.desktop(
                manager = manager,
//                isLineBreakKey = ::isLineBreak,
                isEmptyErase = ::emptyErase,
            )
        )

    private fun emptyErase(
        keyEvent: androidx.compose.ui.input.key.KeyEvent,
        input: TextFieldValue
    ): Boolean =
        keyEvent.awtEventOrNull?.keyCode == KeyEvent.VK_BACK_SPACE &&
                input.selection.start == 0
}

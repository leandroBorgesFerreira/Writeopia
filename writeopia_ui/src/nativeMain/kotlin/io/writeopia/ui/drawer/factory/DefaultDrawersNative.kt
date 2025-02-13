package io.writeopia.ui.drawer.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawConfig

object DefaultDrawersNative : DrawersFactory {

    @Composable
    override fun create(
        manager: WriteopiaStateManager,
        defaultBorder: Shape,
        editable: Boolean,
        groupsBackgroundColor: Color,
        onHeaderClick: () -> Unit,
        drawConfig: DrawConfig,
        fontFamily: FontFamily?,
        onDocumentLinkClick: (String) -> Unit,
    ): Map<Int, StoryStepDrawer> {
        val commonDrawers = CommonDrawers.create(
            manager,
            marginAtBottom = 500.dp,
            defaultBorder,
            editable,
            onHeaderClick,
            lineBreakByContent = true,
            drawConfig = drawConfig,
            isDesktop = false,
            eventListener = KeyEventListenerFactory.android(
                manager,
                isEmptyErase = { _, _ -> false }
            ),
            onDocumentLinkClick = onDocumentLinkClick
        )

        return commonDrawers
    }

//    private fun emptyErase(
//        keyEvent: androidx.compose.ui.input.key.KeyEvent,
//        input: TextFieldValue
//    ): Boolean =
//        keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && input.selection.start == 0
}

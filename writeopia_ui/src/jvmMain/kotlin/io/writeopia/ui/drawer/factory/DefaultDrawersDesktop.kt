package io.writeopia.ui.drawer.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawConfig

object DefaultDrawersDesktop : DrawersFactory {

    @Composable
    override fun create(
        manager: WriteopiaStateManager,
        defaultBorder: Shape,
        editable: Boolean,
        groupsBackgroundColor: Color,
        onHeaderClick: () -> Unit,
        selectedColor: Color,
        selectedBorderColor: Color,
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
            isDesktop = true,
            drawConfig = DrawConfig(
                selectedColor = { selectedColor },
                selectedBorderColor = { selectedBorderColor }
            ),
            eventListener = KeyEventListenerFactory.desktop(
                manager = manager,
//                isLineBreakKey = ::isLineBreak,
            )
        )
}

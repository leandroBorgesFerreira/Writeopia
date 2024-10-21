package io.writeopia.ui.drawer.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.edition.TextCommandHandler
import io.writeopia.ui.manager.WriteopiaStateManager
import kotlinx.coroutines.flow.StateFlow

object DefaultDrawersDesktop : DrawersFactory {

    @Composable
    override fun create(
        manager: WriteopiaStateManager,
        defaultBorder: Shape,
        editable: Boolean,
        groupsBackgroundColor: Color,
        onHeaderClick: () -> Unit,
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
            )
        )
}

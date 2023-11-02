package io.writeopia.sdk.drawer.factory

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.drawer.SimpleTextDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.content.swipeTextDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryTypes

@Composable
fun defaultHxDrawers(
    manager: WriteopiaManager,
    modifier: Modifier = Modifier,
    dragIconWidth: Dp = 16.dp,
    messageDrawer: @Composable RowScope.(TextUnit) -> SimpleTextDrawer
): Map<Int, StoryStepDrawer> {
    val createHDrawer = @Composable { fontSize: TextUnit ->
        swipeTextDrawer(
            modifier = modifier,
            onSelected = manager::onSelected,
            messageDrawer = {
                messageDrawer(fontSize)
            },
            dragIconWidth = dragIconWidth
        )
    }

    val h1MessageDrawer = createHDrawer(28.sp)
    val h2MessageDrawer = createHDrawer(24.sp)
    val h3MessageDrawer = createHDrawer(20.sp)
    val h4MessageDrawer = createHDrawer(18.sp)

    return mapOf(
        StoryTypes.H1.type.number to h1MessageDrawer,
        StoryTypes.H2.type.number to h2MessageDrawer,
        StoryTypes.H3.type.number to h3MessageDrawer,
        StoryTypes.H4.type.number to h4MessageDrawer,
    )
}
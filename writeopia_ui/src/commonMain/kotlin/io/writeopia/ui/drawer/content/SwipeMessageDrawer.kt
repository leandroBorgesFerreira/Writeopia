package io.writeopia.ui.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.manager.WriteopiaStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Draw a text that can be edited with a swipe effect to trigger edition.
 */
fun swipeTextDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color = Color.Transparent,
    clickable: Boolean = true,
    dragIconWidth: Dp = 16.dp,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    onDragHover: (Int) -> Unit,
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
): StoryStepDrawer =
    TextItemDrawer(
        modifier,
        customBackgroundColor,
        clickable,
        onSelected,
        dragIconWidth,
        onDragHover,
        null,
        messageDrawer
    )

fun swipeTextDrawer(
    manager: WriteopiaStateManager,
    modifier: Modifier = Modifier,
    dragIconWidth: Dp = 16.dp,
    onDragHover: (Int) -> Unit,
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
): StoryStepDrawer {
    return swipeTextDrawer(
        modifier = modifier,
        onSelected = manager::onSelected,
        dragIconWidth = dragIconWidth,
        onDragHover = onDragHover,
        customBackgroundColor = Color.Transparent,
        messageDrawer = {
            messageDrawer()
        }
    )
}

@Preview
@Composable
private fun SwipeMessageDrawerPreview() {
    swipeTextDrawer(
        onDragHover = {},
        messageDrawer = {
            TextDrawer(selectionState = MutableStateFlow(false), onSelectionLister = {})
        },
    ).Step(
        step = StoryStep(text = "Some text", type = StoryTypes.TEXT.type),
        drawInfo = DrawInfo(selectMode = true),
    )
}

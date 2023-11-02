package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.writeopia.sdk.drawer.SimpleTextDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep

/**
 * Drawer for a complex message with swipe action, drag and drop logic and a start content to add functionality
 * like a checkbox or a different Composable.
 */
actual class TextItemDrawer actual constructor(
    modifier: Modifier,
    customBackgroundColor: Color,
    clickable: Boolean,
    onSelected: (Boolean, Int) -> Unit,
    focusRequester: FocusRequester?,
    dragIconWidth: Dp,
    startContent: @Composable ((StoryStep, DrawInfo) -> Unit)?,
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        TODO("Not yet implemented")
    }
}
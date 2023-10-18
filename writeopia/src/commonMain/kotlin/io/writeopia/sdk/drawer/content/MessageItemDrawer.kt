package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.uicomponents.SwipeBox

/**
 * Drawer for a complex message with swipe action, drag and drop logic and a start content to add functionality
 * like a checkbox or a different Composable.
 */
expect class MessageItemDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color = Color.Transparent,
    clickable: Boolean = true,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    focusRequester: FocusRequester? = null,
    dragIconWidth: Dp = 16.dp,
    startContent: @Composable ((StoryStep, DrawInfo) -> Unit)?,
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
) : StoryStepDrawer
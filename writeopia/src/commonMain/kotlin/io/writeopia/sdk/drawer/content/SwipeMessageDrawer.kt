package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import io.writeopia.sdk.draganddrop.target.DragTargetWithDragItem
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.uicomponents.SwipeBox

/**
 * Draw a text that can be edited. The edition of the text is both reflect in this Composable and
 * also notified by onTextEdit. It is necessary to reflect here to avoid losing the focus on the
 * TextField.
 */
fun swipeMessageDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color? = null,
    clickable: Boolean = true,
    focusRequester: FocusRequester,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer =
    ListItemDrawer(modifier, customBackgroundColor, clickable, onSelected, focusRequester, null, messageDrawer)
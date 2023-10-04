package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer

/**
 * Draw a text that can be edited with a swipe effect to trigger edition.
 */
fun swipeMessageDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color? = null,
    clickable: Boolean = true,
    focusRequester: FocusRequester,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer =
    MessageItemDrawer(modifier, customBackgroundColor, clickable, onSelected, focusRequester, null, messageDrawer)
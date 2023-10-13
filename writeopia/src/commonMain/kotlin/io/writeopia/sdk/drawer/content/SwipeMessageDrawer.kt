package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.manager.WriteopiaManager

/**
 * Draw a text that can be edited with a swipe effect to trigger edition.
 */
fun swipeMessageDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color = Color.Transparent,
    clickable: Boolean = true,
    focusRequester: FocusRequester,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer =
    MessageItemDrawer(modifier, customBackgroundColor, clickable, onSelected, focusRequester, null, messageDrawer)

@Composable
fun swipeMessageDrawer(
    manager: WriteopiaManager,
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer {
    val focusRequesterSwipeMessage = remember { FocusRequester() }
    return swipeMessageDrawer(
        modifier = Modifier.padding(horizontal = 12.dp),
        onSelected = manager::onSelected,
        focusRequester = focusRequesterSwipeMessage,
        customBackgroundColor = Color.Transparent,
        messageDrawer = {
            messageDrawer()
        }
    )
}
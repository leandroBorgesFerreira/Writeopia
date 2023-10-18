package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep

/**
 * Drawer for a unordered list. This type of item it just a normal message with some decoration
 * at the start of Composable to show that this is part of a list.
 */
fun unOrderedListItemDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color = Color.Transparent,
    clickable: Boolean = true,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    focusRequester: FocusRequester? = null,
    dragIconWidth: Dp = 16.dp,
    startContent: @Composable ((StoryStep, DrawInfo) -> Unit)? = { _, _ ->
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = "-",
        )
    },
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer =
    MessageItemDrawer(
        modifier,
        customBackgroundColor,
        clickable,
        onSelected,
        focusRequester,
        dragIconWidth,
        startContent,
        messageDrawer
    )

@Composable
fun unOrderedListItemDrawer(
    manager: WriteopiaManager,
    modifier: Modifier = Modifier,
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer {
    val focusRequesterUnOrderedList = remember { FocusRequester() }

    return unOrderedListItemDrawer(
        modifier = modifier,
        onSelected = manager::onSelected,
        focusRequester = focusRequesterUnOrderedList,
        customBackgroundColor = Color.Transparent,
        messageDrawer = messageDrawer,
    )
}
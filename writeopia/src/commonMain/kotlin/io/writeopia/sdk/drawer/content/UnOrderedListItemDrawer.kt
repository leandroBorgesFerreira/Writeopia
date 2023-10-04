package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep

/**
 * Drawer for a unordered list. This type of item it just a normal message with some decoration
 * at the start of Composable to show that this is part of a list.
 */
fun unOrderedListItemDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color? = null,
    clickable: Boolean = true,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    focusRequester: FocusRequester? = null,
    startContent: @Composable ((StoryStep, DrawInfo) -> Unit)? = { _, _ ->
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "-",
        )
    },
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer =
    MessageItemDrawer(modifier, customBackgroundColor, clickable, onSelected, focusRequester, startContent, messageDrawer)

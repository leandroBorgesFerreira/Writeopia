package io.writeopia.ui.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
 * Drawer for a unordered list. This type of item it just a normal message with some decoration
 * at the start of Composable to show that this is part of a list.
 */

fun unOrderedListItemDrawer(
    manager: WriteopiaStateManager,
    modifier: Modifier = Modifier,
    dragIconWidth: Dp = 16.dp,
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
): StoryStepDrawer = unOrderedListItemDrawer(
    modifier = modifier,
    onSelected = manager::onSelected,
    customBackgroundColor = Color.Transparent,
    dragIconWidth = dragIconWidth,
    messageDrawer = messageDrawer,
)

fun unOrderedListItemDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color = Color.Transparent,
    clickable: Boolean = true,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    dragIconWidth: Dp = 16.dp,
    startContent: @Composable ((StoryStep, DrawInfo) -> Unit)? = { _, _ ->
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = "-",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    },
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
): StoryStepDrawer =
    TextItemDrawer(
        modifier,
        customBackgroundColor,
        clickable,
        onSelected,
        dragIconWidth,
        startContent,
        messageDrawer
    )

@Preview
@Composable
private fun UnOrderedListItemPreview() {
    val modifier = Modifier
        .background(Color.White)
        .padding(vertical = 4.dp, horizontal = 6.dp)
        .fillMaxWidth()

    unOrderedListItemDrawer(
        modifier,
        messageDrawer = {
            TextDrawer(
                selectionState = MutableStateFlow(false),
                onSelectionLister = {}
            )
        }
    ).Step(
        StoryStep(type = StoryTypes.UNORDERED_LIST_ITEM.type, text = "Item1"),
        DrawInfo()
    )
}


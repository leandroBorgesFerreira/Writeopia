package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.SimpleTextDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep

/**
 * Check item drawer. Draws a checkbox followed by a text.
 */
@OptIn(ExperimentalMaterial3Api::class)
fun checkItemDrawer(
    modifier: Modifier = Modifier,
    customBackgroundColor: Color = Color.Transparent,
    clickable: Boolean = true,
    onSelected: (Boolean, Int) -> Unit = { _, _ -> },
    dragIconWidth: Dp = 16.dp,
    onCheckedChange: (Action.StoryStateChange) -> Unit = {},
    startContent: @Composable ((StoryStep, DrawInfo) -> Unit)? = { step, drawInfo ->
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                modifier = Modifier.scale(0.8F),
                checked = step.checked ?: false,
                onCheckedChange = { checked ->
                    onCheckedChange(
                        Action.StoryStateChange(
                            step.copy(checked = checked),
                            drawInfo.position
                        )
                    )
                },
                enabled = drawInfo.editable,
            )
        }
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

@Composable
fun checkItemDrawer(
    manager: WriteopiaManager,
    modifier: Modifier = Modifier,
    dragIconWidth: Dp = 16.dp,
    messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
): StoryStepDrawer = checkItemDrawer(
        modifier = modifier,
        onCheckedChange = manager::changeStoryState,
        onSelected = manager::onSelected,
        customBackgroundColor = Color.Transparent,
        dragIconWidth = dragIconWidth,
        messageDrawer = {
            messageDrawer()
        },
    )

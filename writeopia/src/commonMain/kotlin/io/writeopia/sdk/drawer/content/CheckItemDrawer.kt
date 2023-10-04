package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.SimpleMessageDrawer
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.factory.DrawersConfig
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
    focusRequester: FocusRequester? = null,
    onCheckedChange: (Action.StoryStateChange) -> Unit = {},
    startContent: @Composable ((StoryStep, DrawInfo) -> Unit)? = { step, drawInfo ->
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                modifier = Modifier.padding(start = 4.dp).scale(0.66F),
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
    messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer
): StoryStepDrawer =
    MessageItemDrawer(
        modifier,
        customBackgroundColor,
        clickable,
        onSelected,
        focusRequester,
        startContent,
        messageDrawer
    )

fun checkItemDrawer(drawersConfig: DrawersConfig, messageDrawer: @Composable RowScope.() -> SimpleMessageDrawer): StoryStepDrawer {
    val focusRequesterCheckItem = remember { FocusRequester() }
    return checkItemDrawer(
        modifier = Modifier.padding(start = 18.dp, end = 12.dp),
        onCheckedChange = drawersConfig.checkRequest,
        onSelected = drawersConfig.onSelected,
        customBackgroundColor = Color.Transparent,
        focusRequester = focusRequesterCheckItem,
        messageDrawer = {
            messageDrawer()
        },
    )
}


package io.writeopia.ui.drawer.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.StoryStepDrawer
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The drawer to a check item in the preview of a note.
 */
class CheckItemPreviewDrawer(
    private val modifier: Modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
) : StoryStepDrawer {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val textColor = MaterialTheme.colorScheme.onBackground
        val fontSize = 12.sp

        val textStyle = if (step.checked == true) {
            MaterialTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.LineThrough,
                fontSize = fontSize
            )
        } else {
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = fontSize
            )
        }

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentEnforcement provides false
            ) {
                Checkbox(
                    modifier = Modifier.size(25.dp),
                    checked = step.checked == true,
                    onCheckedChange = {},
                    enabled = false,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = step.text ?: "",
                modifier = Modifier.padding(vertical = 5.dp),
                style = textStyle,
                color = textColor,
            )
        }
    }
}

@Preview
@Composable
private fun CheckItemPreviewDrawerPreview() {
    Box(modifier = Modifier.background(Color.Cyan)) {
        CheckItemPreviewDrawer().Step(
            step = StoryStep(
                type = StoryTypes.CHECK_ITEM.type,
                text = "Check item"
            ),
            drawInfo = DrawInfo()
        )
    }
}

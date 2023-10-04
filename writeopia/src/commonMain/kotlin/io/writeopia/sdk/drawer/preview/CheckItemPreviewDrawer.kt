package io.writeopia.sdk.drawer.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep

/**
 * The drawer to a check item in the preview of a note.
 */
class CheckItemPreviewDrawer(
    private val modifier: Modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
) : StoryStepDrawer {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val textStyle = if (step.checked == true) {
            MaterialTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.LineThrough,
                fontSize = 15.sp
            )
        } else {
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp
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
                    checked = step.checked ?: false,
                    onCheckedChange = {},
                    enabled = false,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = step.text ?: "",
                modifier = Modifier.padding(vertical = 5.dp),
                style = textStyle,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
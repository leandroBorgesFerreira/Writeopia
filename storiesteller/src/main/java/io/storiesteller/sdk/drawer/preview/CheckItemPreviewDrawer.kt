package io.storiesteller.sdk.drawer.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.storiesteller.sdk.drawer.DrawInfo
import io.storiesteller.sdk.drawer.StoryStepDrawer
import io.storiesteller.sdk.models.story.StoryStep
import io.storiesteller.sdk.model.story.StoryTypes

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

@Preview
@Composable
private fun CheckItemPreviewDrawerPreview() {
    Box(modifier = Modifier.background(Color.Cyan)) {
        CheckItemPreviewDrawer().Step(
            step = StoryStep(
                type = StoryTypes.CHECK_ITEM.type,
                text = "Check item"
            ), drawInfo = DrawInfo()
        )
    }
}
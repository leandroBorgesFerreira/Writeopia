
package io.writeopia.ui.drawer.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.StoryStepDrawer
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The drawer to the header of a document in the preview of a note.
 */
class HeaderPreviewDrawer(
    private val modifier: Modifier = Modifier,
    private val style: TextStyle? = null
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val textColor = MaterialTheme.colorScheme.onBackground

        Box(
            modifier = modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 70.dp)
                .let { modifierLet ->
                    val backgroundColor = step.decoration.backgroundColor
                    if (backgroundColor != null) {
                        modifierLet.background(Color(backgroundColor))
                    } else {
                        modifierLet
                    }
                }
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .align(Alignment.BottomStart),
                text = step.text ?: "",
                style = style ?: MaterialTheme.typography.titleLarge,
                color = textColor,
            )
        }
    }
}

@Preview
@Composable
fun HeaderPreviewDrawerPreview() {
    HeaderPreviewDrawer().Step(
        step = StoryStep(
            type = StoryTypes.TITLE.type,
            text = "Some title"
        ),
        drawInfo = DrawInfo()
    )
}

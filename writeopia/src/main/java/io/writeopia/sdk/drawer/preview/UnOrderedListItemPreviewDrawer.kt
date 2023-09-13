package io.writeopia.sdk.drawer.preview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.DrawInfo
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.model.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep

class UnOrderedListItemPreviewDrawer(
    private val modifier: Modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp),
    private val textModifier: Modifier = Modifier,
    private val startText: String = "-",
    private val textStyle: @Composable () -> TextStyle = {
        LocalTextStyle.current
    },
    private val maxLines: Int = 1
) : StoryStepDrawer {
    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.padding(end = 6.dp), text = startText, style = textStyle())
            TextPreviewDrawer(
                modifier = textModifier,
                style = textStyle,
                maxLines = maxLines
            ).Step(
                step = step,
                drawInfo = drawInfo
            )
        }
    }
}

@Preview
@Composable
fun UnOrderedListItemPreviewDrawerPreview() {
    Surface {
        UnOrderedListItemPreviewDrawer().Step(
            step = StoryStep(
                type = StoryTypes.MESSAGE.type,
                text = "This is a text list item preview"
            ),
            drawInfo = DrawInfo()
        )
    }
}

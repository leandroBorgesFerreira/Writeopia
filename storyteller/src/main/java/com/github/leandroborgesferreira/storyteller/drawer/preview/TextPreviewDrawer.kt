package com.github.leandroborgesferreira.storyteller.drawer.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryStepDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep


class TextPreviewDrawer(
    private val modifier: Modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp),
    private val style: @Composable () -> TextStyle = {
        MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
    },
    private val maxLines: Int = Int.MAX_VALUE
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        Text(
            modifier = modifier,
            text = step.text ?: "",
            style = style(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = maxLines
        )
    }
}

@Preview
@Composable
fun TextPreviewDrawerPreview() {
    Surface {
        TextPreviewDrawer().Step(
            step = StoryStep(
                type = StoryTypes.MESSAGE.type,
                text = "This is a text message preview"
            ),
            drawInfo = DrawInfo()
        )
    }
}
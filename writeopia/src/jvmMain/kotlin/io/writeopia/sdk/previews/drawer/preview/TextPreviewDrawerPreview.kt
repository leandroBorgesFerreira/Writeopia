package io.writeopia.sdk.previews.drawer.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import io.writeopia.sdk.drawer.preview.TextPreviewDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

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
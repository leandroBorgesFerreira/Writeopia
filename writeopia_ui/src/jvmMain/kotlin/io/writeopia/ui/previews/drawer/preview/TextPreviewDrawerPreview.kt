package io.writeopia.ui.previews.drawer.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.preview.TextPreviewDrawer

@Preview
@Composable
fun TextPreviewDrawerPreview() {
    Surface {
        TextPreviewDrawer().Step(
            step = StoryStep(
                type = StoryTypes.TEXT.type,
                text = "This is a text message preview"
            ),
            drawInfo = DrawInfo()
        )
    }
}
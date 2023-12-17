package io.writeopia.ui.previews.drawer.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import io.writeopia.sdk.drawer.preview.UnOrderedListItemPreviewDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
fun UnOrderedListItemPreviewDrawerPreview() {
    Surface {
        UnOrderedListItemPreviewDrawer().Step(
            step = StoryStep(
                type = StoryTypes.TEXT.type,
                text = "This is a text list item preview"
            ),
            drawInfo = DrawInfo()
        )
    }
}
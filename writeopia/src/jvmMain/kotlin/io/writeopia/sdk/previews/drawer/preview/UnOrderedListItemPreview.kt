package io.writeopia.sdk.previews.drawer.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import io.writeopia.sdk.drawer.content.TextDrawer
import io.writeopia.sdk.drawer.content.unOrderedListItemDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
fun UnOrderedListItemDrawerPreview() {
    Surface {
        unOrderedListItemDrawer(
            messageDrawer = {
                TextDrawer()
            },
        ).Step(
            step = StoryStep(
                type = StoryTypes.TEXT.type,
                text = "This is a text list item preview"
            ),
            drawInfo = DrawInfo()
        )
    }
}
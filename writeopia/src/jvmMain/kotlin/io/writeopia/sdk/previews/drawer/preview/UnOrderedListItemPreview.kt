package io.writeopia.sdk.previews.drawer.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import io.writeopia.sdk.drawer.content.DesktopMessageDrawer
import io.writeopia.sdk.drawer.content.UnOrderedListItemDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
fun UnOrderedListItemDrawerPreview() {
    Surface {
        UnOrderedListItemDrawer(
            messageDrawer = { focusRequester ->
                DesktopMessageDrawer(focusRequester = focusRequester)
            }
        ).Step(
            step = StoryStep(
                type = StoryTypes.MESSAGE.type,
                text = "This is a text list item preview"
            ),
            drawInfo = DrawInfo()
        )
    }
}
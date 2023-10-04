package io.writeopia.sdk.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import io.writeopia.sdk.drawer.content.DesktopMessageDrawer
import io.writeopia.sdk.drawer.content.swipeMessageDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep

@Preview
@Composable
private fun SwipeMessageDrawerPreview() {
    val focusRequester = FocusRequester()

    swipeMessageDrawer(
        messageDrawer = {
            DesktopMessageDrawer(focusRequester = focusRequester)
        },
        focusRequester = focusRequester
    ).Step(
        step = StoryStep(text = "Some text", type = StoryTypes.MESSAGE.type),
        drawInfo = DrawInfo()
    )
}

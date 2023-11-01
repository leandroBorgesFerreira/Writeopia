package io.writeopia.sdk.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import io.writeopia.sdk.drawer.content.MessageDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
fun DesktopMessageDrawerPreview() {
    MessageDrawer(focusRequester = FocusRequester()).Step(
        step = StoryStep(text = "Some text", type = StoryTypes.TEXT.type),
        drawInfo = DrawInfo()
    )
}
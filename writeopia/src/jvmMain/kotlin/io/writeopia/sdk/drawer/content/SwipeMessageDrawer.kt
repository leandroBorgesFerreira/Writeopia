package io.writeopia.sdk.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep

@Preview
@Composable
private fun MessageDrawerPreview() {
    SwipeMessageDrawer().Step(
        step = StoryStep(text = "Some text", type = StoryTypes.MESSAGE.type),
        drawInfo = DrawInfo()
    )
}

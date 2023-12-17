package io.writeopia.ui.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.writeopia.sdk.drawer.content.TextDrawer
import io.writeopia.sdk.drawer.content.swipeTextDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep

@Preview
@Composable
private fun SwipeMessageDrawerPreview() {
    swipeTextDrawer(
        messageDrawer = {
            TextDrawer()
        },
    ).Step(
        step = StoryStep(text = "Some text", type = StoryTypes.TEXT.type),
        drawInfo = DrawInfo()
    )
}

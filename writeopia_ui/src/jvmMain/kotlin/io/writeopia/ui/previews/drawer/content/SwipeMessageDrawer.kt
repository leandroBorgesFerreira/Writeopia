package io.writeopia.ui.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.content.TextDrawer
import io.writeopia.ui.drawer.content.swipeTextDrawer

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

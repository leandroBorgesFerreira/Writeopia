package io.writeopia.sdk.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.writeopia.sdk.drawer.content.MessageDrawer
import io.writeopia.sdk.drawer.content.checkItemDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
fun CheckItemDrawerStepPreview() {
    checkItemDrawer(messageDrawer = { MessageDrawer() }).Step(
        step = StoryStep(
            type = StoryTypes.CHECK_ITEM.type,
            text = "This is a check item"
        ),
        drawInfo = DrawInfo()
    )
}
package io.writeopia.ui.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.content.TextDrawer
import io.writeopia.ui.drawer.content.checkItemDrawer

@Preview
@Composable
fun CheckItemDrawerStepPreview() {
    checkItemDrawer(messageDrawer = { TextDrawer() }).Step(
        step = StoryStep(
            type = StoryTypes.CHECK_ITEM.type,
            text = "This is a check item"
        ),
        drawInfo = DrawInfo()
    )
}
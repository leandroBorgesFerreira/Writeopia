package io.writeopia.sdk.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
fun TitleDrawerStepPreview() {
    TitleDrawer(
        onTextEdit = { _, _ -> },
        onLineBreak = {}).Step(
        step = StoryStep(
            type = StoryTypes.TITLE.type,
            text = "Some title"
        ), drawInfo = DrawInfo()
    )
}
package io.writeopia.sdk.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.writeopia.sdk.drawer.content.HeaderDrawer
import io.writeopia.sdk.drawer.content.MobileTitleDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
fun HeaderDrawerStepPreview() {
    val step = StoryStep(
        type = StoryTypes.TITLE.type,
        decoration = Decoration(
            backgroundColor = Color.Blue.toArgb()
        ),
        text = "Document Title",
    )

    HeaderDrawer(
        drawer = {
            MobileTitleDrawer(
                modifier = Modifier.align(Alignment.BottomStart),
                onTextEdit = { },
                onLineBreak = {},
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}

@Preview
@Composable
fun HeaderDrawerStepPreviewNoColor() {
    val step = StoryStep(
        type = StoryTypes.TITLE.type,
        decoration = Decoration(),
        text = "Document Title",
    )

    HeaderDrawer(
        drawer = {
            MobileTitleDrawer(
                modifier = Modifier.align(Alignment.BottomStart),
                onTextEdit = { },
                onLineBreak = {},
            )
        },
        headerClick = {}
    ).Step(step = step, drawInfo = DrawInfo())
}
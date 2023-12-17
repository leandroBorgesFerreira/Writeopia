package io.writeopia.ui.previews.drawer.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.preview.HeaderPreviewDrawer

@Preview
@Composable
fun HeaderPreviewDrawerPreview() {
    HeaderPreviewDrawer().Step(
        step = StoryStep(
            type = StoryTypes.TITLE.type,
            text = "Some title"
        ),
        drawInfo = DrawInfo()
    )
}
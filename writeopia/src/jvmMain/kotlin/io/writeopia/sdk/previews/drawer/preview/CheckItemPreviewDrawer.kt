package io.writeopia.sdk.previews.drawer.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.writeopia.sdk.drawer.preview.CheckItemPreviewDrawer
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

@Preview
@Composable
private fun CheckItemPreviewDrawerPreview() {
    Box(modifier = Modifier.background(Color.Cyan)) {
        CheckItemPreviewDrawer().Step(
            step = StoryStep(
                type = StoryTypes.CHECK_ITEM.type,
                text = "Check item"
            ), drawInfo = DrawInfo()
        )
    }
}
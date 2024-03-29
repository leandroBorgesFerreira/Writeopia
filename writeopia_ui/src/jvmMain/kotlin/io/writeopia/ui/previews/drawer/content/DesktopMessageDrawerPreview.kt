package io.writeopia.ui.previews.drawer.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.drawer.content.TextDrawer

@Preview
@Composable
fun DesktopMessageDrawerPreview() {
    TextDrawer().Text(
        step = StoryStep(text = "Some text", type = StoryTypes.TEXT.type),
        drawInfo = DrawInfo(),
        interactionSource = remember { MutableInteractionSource() },
        focusRequester = FocusRequester(),
        decorationBox = @Composable { innerTextField -> innerTextField() }
    )
}
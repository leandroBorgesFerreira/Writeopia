package io.writeopia.sdk.drawer

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusState
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep

interface SimpleTextDrawer {

    var onFocusChanged: (FocusState) -> Unit

    /**
     * Draws the StoryStep including its [DrawInfo]
     *
     * @param step [StoryStep]
     * @param drawInfo [DrawInfo]
     */
    @Composable
    fun Text(
        step: StoryStep,
        drawInfo: DrawInfo,
        interactionSource: MutableInteractionSource,
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit
    )
}
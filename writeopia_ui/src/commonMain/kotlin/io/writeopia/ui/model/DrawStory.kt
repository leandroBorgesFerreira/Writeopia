package io.writeopia.ui.model

import io.writeopia.sdk.model.story.Selection
import io.writeopia.sdk.models.story.StoryStep

/**
 * Class meant to be draw in the screen. It contains both the information of a story step and
 * meta information and the state of the TextEditor like if the message is selected
 */
data class DrawStory(
    val storyStep: StoryStep,
    val cursor: Selection,
    val isSelected: Boolean,
    val position: Int
) {

    val key = storyStep.key + isSelected.let { if (it) 1 else 0 } + cursor.hashCode()
}

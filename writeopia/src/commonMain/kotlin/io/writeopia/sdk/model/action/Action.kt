package io.writeopia.sdk.model.action

import io.writeopia.sdk.models.story.StoryStep

/**
 * An action performed in the text editor.
 */
sealed class Action {
    data class EraseStory(val storyStep: StoryStep, val position: Int) : Action()

    data class DeleteStory(val storyStep: StoryStep, val position: Int) : Action()

    data class BulkDelete(val deletedUnits: Map<Int, StoryStep>) : Action()

    data class LineBreak(val storyStep: StoryStep, val position: Int) : Action()

    data class Move(val storyStep: StoryStep, val positionFrom: Int, val positionTo: Int) : Action()

    data class StoryStateChange(
        val storyStep: StoryStep,
        val position: Int,
        val selectionStart: Int? = null,
        val selectionEnd: Int? = null
    ) : Action()

    data class Merge(
        val receiver: StoryStep,
        val sender: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : Action()
}

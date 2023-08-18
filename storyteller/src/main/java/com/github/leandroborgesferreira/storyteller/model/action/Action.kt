package com.github.leandroborgesferreira.storyteller.model.action

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

/*
 * Todo: TextEdit should exist?
 */
sealed class Action {
    data class TextEdit(val text: String, val position: Int) : Action()
    data class AddStory(val storyStep: StoryStep, val position: Int) : Action()
    data class DeleteStory(val storyStep: StoryStep, val position: Int) : Action()
    data class BulkDelete(val deletedUnits: Map<Int, StoryStep>) : Action()

    data class LineBreak(val storyStep: StoryStep, val position: Int) : Action()
    data class Move(val storyStep: StoryStep, val positionFrom: Int, val positionTo: Int) : Action()

    data class StoryStateChange(val storyStep: StoryStep, val position: Int): Action()

    data class Merge(
        val receiver: StoryStep,
        val sender: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : Action()

}
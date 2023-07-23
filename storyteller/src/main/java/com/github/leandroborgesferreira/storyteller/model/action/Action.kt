package com.github.leandroborgesferreira.storyteller.model.action

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

sealed class Action {
    data class TextEdit(val text: String, val position: Int) : Action()
    data class AddStory(val storyUnit: StoryStep, val position: Int) : Action()
    data class DeleteStory(val storyUnit: StoryStep, val position: Int) : Action()
    data class BulkDelete(val deletedUnits: Map<Int, StoryStep>) : Action()
    data class Check(val storyUnit: StoryStep, val position: Int, val checked: Boolean) : Action()
    data class LineBreak(val storyStep: StoryStep, val position: Int) : Action()
    data class Move(val storyUnit: StoryStep, val positionFrom: Int, val positionTo: Int) : Action()

    data class Merge(
        val receiver: StoryStep,
        val sender: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : Action()

    data class AddText(val text: String, val position: Int, val isComplete: Boolean): Action()
}
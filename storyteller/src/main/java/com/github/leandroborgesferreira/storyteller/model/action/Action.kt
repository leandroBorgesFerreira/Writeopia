package com.github.leandroborgesferreira.storyteller.model.action

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

/*
 * Todo: TextEdit, AddText and Check should probably be merged into the same action.
 * Should LineBreak exist???
 */
sealed class Action {
    data class TextEdit(val text: String, val position: Int) : Action()
    data class AddStory(val storyStep: StoryStep, val position: Int) : Action()
    data class DeleteStory(val storyStep: StoryStep, val position: Int) : Action()
    data class BulkDelete(val deletedUnits: Map<Int, StoryStep>) : Action()

    //Maybe StoryStateChange??
    data class Check(val storyStep: StoryStep, val position: Int, val checked: Boolean) : Action()
    data class LineBreak(val storyStep: StoryStep, val position: Int) : Action()
    data class Move(val storyStep: StoryStep, val positionFrom: Int, val positionTo: Int) : Action()

    data class Merge(
        val receiver: StoryStep,
        val sender: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : Action()

    //Todo: Delete this!!
    data class AddText(val text: String, val position: Int, val isComplete: Boolean): Action()
}
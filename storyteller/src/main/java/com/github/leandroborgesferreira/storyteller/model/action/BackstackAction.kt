package com.github.leandroborgesferreira.storyteller.model.action

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

/**
 * Back stack action, the classes of this sealed class represent that actions that the back stack
 * manager can handle and revert.
 */
sealed class BackstackAction {
    data class StoryStateChange(val storyStep: StoryStep, val position: Int) : BackstackAction()

    data class StoryTextChange(val storyStep: StoryStep, val position: Int) : BackstackAction()

    data class Move(
        val storyStep: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : BackstackAction()

    data class Delete(val storyStep: StoryStep, val position: Int) : BackstackAction()
    data class Add(val storyStep: StoryStep, val position: Int) : BackstackAction()

    data class BulkDelete(val deletedUnits: Map<Int, StoryStep>) : BackstackAction()

    data class Merge(
        val receiver: StoryStep,
        val sender: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : BackstackAction()
}
package com.github.leandroborgesferreira.storyteller.model.action

import com.github.leandroborgesferreira.storyteller.backstack.BackstackManager
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

/**
 * Back stack action, the classes of this sealed class represent that actions that the back stack
 * manager can handle and revert.
 */
sealed class BackstackAction {
    /**
     * A change in the state of the story, with the exception of a text change.
     */
    data class StoryStateChange(
        override val storyStep: StoryStep,
        override val position: Int
    ) : BackstackAction(), SingleAction

    /**
     * A change in the text of the story. There's a separation between [StoryTextChange] and
     * [StoryStateChange] because StoryTextChange and not saved as a unit for usability and
     * performance reasons. A user wouldn't like to revert character by character so the
     * [BackstackManager] must handle merging them.
     */
    data class StoryTextChange(
        override val storyStep: StoryStep,
        override val position: Int
    ) : BackstackAction(), SingleAction

    /**
     * A move of stories
     */
    data class Move(
        val storyStep: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : BackstackAction()

    /**
     * Deleting a single story
     */
    data class Delete(
        override val storyStep: StoryStep,
        override val position: Int
    ) : BackstackAction(), SingleAction

    /**
     * Adding a single story
     */
    data class Add(
        override val storyStep: StoryStep,
        override val position: Int
    ) : BackstackAction(), SingleAction

    /**
     * Deleting many stories
     */
    data class BulkDelete(val deletedUnits: Map<Int, StoryStep>) : BackstackAction()

    /**
     * Merging a single story
     */
    data class Merge(
        val receiver: StoryStep,
        val sender: StoryStep,
        val positionFrom: Int,
        val positionTo: Int
    ) : BackstackAction()
}
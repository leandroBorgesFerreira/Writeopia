package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.manager.ContentHandler
import com.github.leandroborgesferreira.storyteller.manager.MovementHandler
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction
import com.github.leandroborgesferreira.storyteller.model.story.StoryState

/**
 * Manager for backstack.
 */
interface BackstackManager : BackstackInform {

    /**
     * Moves to the previous state available.
     */
    fun previousState(state: StoryState): StoryState

    /**
     * Moves to the next state available. It is only possible to move the next state if the user
     * has undone an action.
     */
    fun nextState(state: StoryState): StoryState

    /**
     * Adds an action to the backstack.
     */
    fun addAction(action: BackstackAction)

    companion object {
        fun create(
            contentHandler: ContentHandler,
            movementHandler: MovementHandler
        ): BackstackManager =
            PerStateBackstackManager(
                contentHandler = contentHandler,
                movementHandler = movementHandler
            )
    }
}
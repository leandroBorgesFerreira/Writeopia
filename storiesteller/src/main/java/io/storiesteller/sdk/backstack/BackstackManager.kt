package io.storiesteller.sdk.backstack

import io.storiesteller.sdk.manager.ContentHandler
import io.storiesteller.sdk.manager.MovementHandler
import io.storiesteller.sdk.model.action.BackstackAction
import io.storiesteller.sdk.model.story.StoryState

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
package io.writeopia.sdk.backstack

import io.writeopia.sdk.manager.ContentHandler
import io.writeopia.sdk.manager.MovementHandler
import io.writeopia.sdk.model.action.BackstackAction
import io.writeopia.sdk.model.story.StoryState

/**
 * Manager for backstack. Implementations of this interface are responsible to coordinate undo and
 * redo requests.
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
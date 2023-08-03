package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.manager.ContentHandler
import com.github.leandroborgesferreira.storyteller.manager.MovementHandler
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction
import com.github.leandroborgesferreira.storyteller.model.story.StoryState

/**
 * Manager for
 */
interface BackstackManager : BackstackInform {

    fun previousState(state: StoryState): StoryState

    fun nextState(state: StoryState): StoryState

    fun addAction(action: BackstackAction)

    companion object {
        fun create(
            contentHandler: ContentHandler,
            movementHandler: MovementHandler = MovementHandler()
        ): BackstackManager =
            PerStateBackstackManager(
                contentHandler = contentHandler,
                movementHandler = movementHandler
            )
    }
}
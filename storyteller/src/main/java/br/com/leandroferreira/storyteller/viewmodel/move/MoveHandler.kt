package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.model.story.StoryUnit

/**
 * Handler for move requests. Implement this interface to add a custom behaviour for move requests.
 * example: scroll to a position after the move, move in a custom way...
 */
interface MoveHandler {

    fun handleMove(
        storyUnits: Map<Int, StoryUnit>,
        moveInfo: MoveInfo
    ): Map<Int, StoryUnit>
}

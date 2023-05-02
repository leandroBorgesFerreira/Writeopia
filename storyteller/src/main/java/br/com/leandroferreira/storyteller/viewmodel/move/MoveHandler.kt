package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.StoryUnit

/**
 * Handler for move requests. Implement this interface to add a custom behaviour for move requests.
 * example: scroll to a position after the move, move in a custom way...
 */
interface MoveHandler {

    fun handleMove(
        storyUnits: List<StoryUnit>,
        storyId: String,
        newPosition: Int
    ): List<StoryUnit>
}

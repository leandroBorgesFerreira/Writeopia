package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.viewmodel.FindStory

/**
 * [SpaceMoveHandler] is a implementation of [MoveHandler] that accepts a move request to a space.
 * Trying to use the [MoveHandler] with a [StoryUnit] different from space will throw an exception.
 *
 * Todo: I need to evaluate sorting the story at the end of the move because this class only works
 * if used with [PositionNormalization]
 */
class SpaceMoveHandler : MoveHandler {

    override fun handleMove(
        storyUnits: List<StoryUnit>,
        storyId: String,
        newPosition: Int
    ): List<StoryUnit> {
        val space = storyUnits[newPosition]

        if (space.type != "space") throw IllegalStateException(
            "StoryUnits can only be moved to space positions the position $newPosition " +
                "doesn't belong to a space"
        )

        val mutable = storyUnits.toMutableList()

        FindStory.findById(storyUnits, storyId)
            ?.takeIf { (storyToMove, _) -> storyToMove != null }
            ?.let { (storyToMove, containerGroup) ->
                if (containerGroup != null) {
                    // Remove StoryUnit from container
                    val newSteps = containerGroup.steps
                        .filter { storyUnit -> storyUnit.id != storyId }

                    mutable[containerGroup.localPosition] =
                        containerGroup.copy(steps = newSteps)
                } else {
                    mutable.removeAt(storyToMove!!.localPosition)
                }

                // Add to the end of list. A reorder WILL be necessary
                mutable.add(storyToMove!!.copyWithNewPosition(newPosition))
            }

        return mutable.toList()
    }

}

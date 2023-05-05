package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.viewmodel.FindStory

/**
 * [SpaceMoveHandler] is a implementation of [MoveHandler] that accepts a move request to a space.
 * Trying to use the [MoveHandler] with a [StoryUnit] different from space will throw an exception.
 */
class SpaceMoveHandler : MoveHandler {

    override fun handleMove(
        storyUnits: Map<Int, StoryUnit>,
        moveInfo: MoveInfo
    ): Map<Int, StoryUnit> {
        val mutable = storyUnits.toMutableMap()
        val space = storyUnits[moveInfo.positionTo]

        if (space?.type != "space") throw IllegalStateException(
            "StoryUnits can only be moved to space positions the position ${moveInfo.positionTo} " +
                "doesn't belong to a space"
        )

        val parentId = moveInfo.storyUnit.parentId

        if (parentId == null) {
            mutable.remove(moveInfo.positionFrom)
            mutable[moveInfo.positionTo] = moveInfo.storyUnit
        } else {
            FindStory.findById(storyUnits.values, parentId)
                ?.takeIf { (storyToMove, _) -> storyToMove != null }
                ?.let { (containerGroup, _) ->
                    if (containerGroup != null && containerGroup is GroupStep) {
                        // Remove StoryUnit from container
                        val newSteps = containerGroup.steps
                            .filter { storyUnit -> storyUnit.id != moveInfo.storyUnit.id }

                        mutable[containerGroup.localPosition] =
                            containerGroup.copy(steps = newSteps)
                    }

                    mutable[moveInfo.positionTo] = moveInfo.storyUnit
                }
        }

        return mutable
    }

}

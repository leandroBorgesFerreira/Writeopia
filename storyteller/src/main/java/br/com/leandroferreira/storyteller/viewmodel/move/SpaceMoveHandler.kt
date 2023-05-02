package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.viewmodel.FindStory

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
                    val newSteps = containerGroup.steps
                        .filter { storyUnit -> storyUnit.id != storyId }

                    mutable[containerGroup.localPosition] =
                        containerGroup.copy(steps = newSteps)
                } else {
                    mutable.removeAt(storyToMove!!.localPosition)
                }

                mutable.add(storyToMove!!.copyWithNewPosition(newPosition))
            }

        return mutable.toList()
    }
}

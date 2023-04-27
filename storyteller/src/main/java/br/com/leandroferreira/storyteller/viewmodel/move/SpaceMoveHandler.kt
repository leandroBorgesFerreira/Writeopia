package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.viewmodel.FindStory

class SpaceMoveHandler : MoveHandler {

    override fun handleMove(
        storyUnits: MutableMap<Int, StoryUnit>,
        storyId: String,
        newPosition: Int
    ): MutableMap<Int, StoryUnit> {
        val space = storyUnits[newPosition]

        if (space?.type != "space") throw IllegalStateException(
            "StoryUnits can only be moved to space positions the position $newPosition " +
                "doesn't belong to a space"
        )

        FindStory.findById(storyUnits, storyId)
            ?.takeIf { (storyToMove, _) -> storyToMove != null }
            ?.let { (storyToMove, containerGroup) ->
                if (containerGroup != null) {
                    val newSteps = containerGroup.steps
                        .filter { storyUnit -> storyUnit.id != storyId }
                    storyUnits[containerGroup.localPosition] = containerGroup.copy(steps = newSteps)
                } else {
                    storyUnits.remove(storyToMove!!.localPosition)
                }

                storyUnits[newPosition] = storyToMove!!.copyWithNewPosition(newPosition)
            }

        return storyUnits
    }
}

package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.GroupStep
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

        val storyToMove = FindStory.findById(storyUnits, storyId)

        storyToMove?.copyWithNewPosition(newPosition)?.let { storyUnit ->
            storyUnits[newPosition] = storyUnit
        }

        return storyUnits
    }
}

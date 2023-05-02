package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.StoryUnit

interface MoveHandler {

    fun handleMove(
        storyUnits: List<StoryUnit>,
        storyId: String,
        newPosition: Int
    ): List<StoryUnit>
}

package br.com.leandroferreira.storyteller.viewmodel.move

import br.com.leandroferreira.storyteller.model.StoryUnit

interface MoveHandler {

    fun handleMove(
        storyUnits: MutableMap<Int, StoryUnit>,
        storyId: String,
        newPosition: Int
    ): MutableMap<Int, StoryUnit>
}

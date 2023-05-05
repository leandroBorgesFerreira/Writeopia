package br.com.leandroferreira.storyteller.model.change

import br.com.leandroferreira.storyteller.model.story.StoryUnit

data class MoveInfo(val storyUnit: StoryUnit, val positionFrom: Int, val positionTo: Int)

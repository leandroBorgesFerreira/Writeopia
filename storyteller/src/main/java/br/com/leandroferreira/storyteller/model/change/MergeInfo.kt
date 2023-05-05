package br.com.leandroferreira.storyteller.model.change

import br.com.leandroferreira.storyteller.model.story.StoryUnit

data class MergeInfo(
    val receiver: StoryUnit,
    val sender: StoryUnit,
    val positionFrom: Int,
    val positionTo: Int
)

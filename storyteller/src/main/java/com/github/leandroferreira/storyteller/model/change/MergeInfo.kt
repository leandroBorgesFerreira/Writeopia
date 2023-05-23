package com.github.leandroferreira.storyteller.model.change

import com.github.leandroferreira.storyteller.model.story.StoryUnit

data class MergeInfo(
    val receiver: StoryUnit,
    val sender: StoryUnit,
    val positionFrom: Int,
    val positionTo: Int
)

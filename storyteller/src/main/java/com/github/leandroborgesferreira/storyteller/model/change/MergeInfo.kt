package com.github.leandroborgesferreira.storyteller.model.change

import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

data class MergeInfo(
    val receiver: StoryUnit,
    val sender: StoryUnit,
    val positionFrom: Int,
    val positionTo: Int
)

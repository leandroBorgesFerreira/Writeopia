package com.github.leandroborgesferreira.storyteller.model.change

import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

data class MoveInfo(val storyUnit: StoryUnit, val positionFrom: Int, val positionTo: Int)

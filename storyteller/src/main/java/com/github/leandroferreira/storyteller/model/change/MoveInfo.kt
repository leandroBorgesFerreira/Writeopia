package com.github.leandroferreira.storyteller.model.change

import com.github.leandroferreira.storyteller.model.story.StoryUnit

data class MoveInfo(val storyUnit: StoryUnit, val positionFrom: Int, val positionTo: Int)

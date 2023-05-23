package com.github.leandroferreira.storyteller.model.change

import com.github.leandroferreira.storyteller.model.story.StoryUnit

data class CheckInfo(val storyUnit: StoryUnit, val position: Int, val checked: Boolean)

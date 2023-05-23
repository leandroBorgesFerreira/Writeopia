package com.github.leandroborgesferreira.storyteller.model.change

import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

data class CheckInfo(val storyUnit: StoryUnit, val position: Int, val checked: Boolean)

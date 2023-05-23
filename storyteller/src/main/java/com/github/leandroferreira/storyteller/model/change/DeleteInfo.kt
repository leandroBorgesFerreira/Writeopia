package com.github.leandroferreira.storyteller.model.change

import com.github.leandroferreira.storyteller.model.story.StoryUnit

data class DeleteInfo(val storyUnit: StoryUnit, val position: Int)

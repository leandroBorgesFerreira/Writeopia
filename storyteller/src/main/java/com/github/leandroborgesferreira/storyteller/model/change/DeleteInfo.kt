package com.github.leandroborgesferreira.storyteller.model.change

import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

data class DeleteInfo(val storyUnit: StoryUnit, val position: Int)

package com.github.leandroborgesferreira.storyteller.model.story

data class StoryState(
    val stories: Map<Int, StoryStep>,
    val focusId: String? = null
)

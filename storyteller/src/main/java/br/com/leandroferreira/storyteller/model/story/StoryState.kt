package br.com.leandroferreira.storyteller.model.story

data class StoryState(
    val stories: Map<Int, StoryUnit>,
    val focusId: String? = null
)

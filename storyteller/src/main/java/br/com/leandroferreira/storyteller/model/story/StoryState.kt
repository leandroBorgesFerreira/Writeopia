package br.com.leandroferreira.storyteller.model.story

data class StoryState(
    val stories: List<StoryUnit>,
    val focusId: String? = null
)
data class StoryStateMap(
    val stories: Map<Int, StoryUnit>,
    val focusId: String? = null
)

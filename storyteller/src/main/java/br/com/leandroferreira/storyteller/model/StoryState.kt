package br.com.leandroferreira.storyteller.model

data class StoryState(val stories: List<StoryUnit>, val scrollTo: Int? = null)

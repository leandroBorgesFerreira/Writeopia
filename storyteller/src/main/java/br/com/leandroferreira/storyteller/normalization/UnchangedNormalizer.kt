package br.com.leandroferreira.storyteller.normalization

import br.com.leandroferreira.storyteller.model.StoryUnit

object UnchangedNormalizer {

    fun skipChange(stories: List<StoryUnit>, filterRepeated: Boolean): List<StoryUnit> = stories
}

package br.com.leandroferreira.storyteller.normalization.sort

import br.com.leandroferreira.storyteller.model.StoryUnit

object SortNormalization {

    fun sort(stories: List<StoryUnit>): List<StoryUnit> = stories.sorted()
}

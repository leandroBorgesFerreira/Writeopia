package br.com.leandroferreira.storyteller.normalization.sort

import br.com.leandroferreira.storyteller.model.StoryUnit

/**
 * Sorts all StoryUnit.
 */
object SortNormalization {

    fun sort(stories: Iterable<StoryUnit>): List<StoryUnit> = stories.sorted()
}

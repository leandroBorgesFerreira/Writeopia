package br.com.leandroferreira.storyteller.normalization.sort

import br.com.leandroferreira.storyteller.model.StoryUnit

/**
 * Sorts all StoryUnit.
 */
object SortNormalization {


    fun sort(stories: Iterable<StoryUnit>): List<StoryUnit> = stories.sorted()

    // Todo: It is necessary to sort a map?
    fun sort(stories: Map<Int, List<StoryUnit>>): Map<Int, List<StoryUnit>> = stories.toSortedMap()

}

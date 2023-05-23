package com.github.leandroferreira.storyteller.normalization.unchange

import com.github.leandroferreira.storyteller.model.story.StoryUnit

/**
 * Doesn't change the current list. This Normalizer is intended for mocking purposes.
 */
object UnchangedNormalizer {

    fun skipChange(stories: Iterable<StoryUnit>): List<StoryUnit> = stories.toList()

    fun skipChange(stories: Map<Int, StoryUnit>): Map<Int, StoryUnit> = stories
}

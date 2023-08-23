package com.github.leandroborgesferreira.storyteller.normalization.unchange

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

/**
 * Doesn't change the current list. This Normalizer is intended for mocking purposes.
 */
object UnchangedNormalizer {

    fun skipChange(stories: Iterable<StoryStep>): List<StoryStep> = stories.toList()

    fun skipChange(stories: Map<Int, StoryStep>): Map<Int, StoryStep> = stories
}

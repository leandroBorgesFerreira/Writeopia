package com.github.leandroborgesferreira.storyteller.preview

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes

class PreviewParser(
    private val acceptedTypes: Set<Int> = defaultTypes()
) {
    fun preview(stories: Iterable<StoryStep>, maxSize: Int = 4): List<StoryStep> {
        var acc = 0

        return stories.asSequence()
            .filter { storyStep -> acceptedTypes.contains(storyStep.type.number)  }
            .takeWhile { acc++ < maxSize }
            .toList()
    }
}

private fun defaultTypes() = setOf(
    StoryTypes.TITLE.type.number,
    StoryTypes.MESSAGE.type.number,
    StoryTypes.H1.type.number,
    StoryTypes.H2.type.number,
    StoryTypes.H3.type.number,
    StoryTypes.H4.type.number,
    StoryTypes.CHECK_ITEM.type.number,
)

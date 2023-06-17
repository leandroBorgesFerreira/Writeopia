package com.github.leandroborgesferreira.storyteller.parse

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

class PreviewParser(private val acceptedTypes: Set<String>) {

    fun preview(stories: Iterable<StoryStep>): List<StoryStep> {
        var acc = 0

        return stories.asSequence()
            .filter { storyStep -> acceptedTypes.contains(storyStep.type)  }
            .takeWhile { acc++ < 4 }
            .toList()
    }
}

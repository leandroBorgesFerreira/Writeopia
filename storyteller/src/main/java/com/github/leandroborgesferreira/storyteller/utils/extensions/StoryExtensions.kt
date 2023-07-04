package com.github.leandroborgesferreira.storyteller.utils.extensions

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

fun Map<Int, StoryStep>.toEditState(): MutableMap<Int, List<StoryStep>> =
    mapValues { (_, story) -> listOf(story) }.toMutableMap()

fun <T> Iterable<T>.associateWithPosition(): Map<Int, T> {
    var acc = -1

    return associateBy { ++acc }
}

fun Map<Int, StoryStep>.noContent(): Boolean =
    this.values.any { storyStep ->
        storyStep.run {
            url.isNullOrBlank() &&
                    path.isNullOrBlank() &&
                    text.isNullOrBlank() &&
                    title.isNullOrBlank() &&
                    steps.isEmpty()
        }
    }

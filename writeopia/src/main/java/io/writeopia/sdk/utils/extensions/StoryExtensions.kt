package io.writeopia.sdk.utils.extensions

import io.writeopia.sdk.models.story.StoryStep

fun Map<Int, StoryStep>.toEditState(): MutableMap<Int, List<StoryStep>> =
    mapValues { (_, story) -> listOf(story) }.toMutableMap()

fun <T> Iterable<T>.associateWithPosition(): Map<Int, T> {
    var acc = -1

    return associateBy { ++acc }
}

fun Map<Int, StoryStep>.noContent(): Boolean =
    this.values.all { storyStep ->
        storyStep.run {
            url.isNullOrBlank() &&
                    path.isNullOrBlank() &&
                    text.isNullOrBlank() &&
                    steps.isEmpty()
        }
    }

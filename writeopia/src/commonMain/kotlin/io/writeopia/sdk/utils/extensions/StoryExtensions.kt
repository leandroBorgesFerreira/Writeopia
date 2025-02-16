package io.writeopia.sdk.utils.extensions

import io.writeopia.sdk.models.id.GenerateId
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

fun Map<Int, StoryStep>.previousTextStory(
    position: Int,
    isTextStory: (StoryStep) -> Boolean
): Pair<StoryStep, Int>? {
    var acc = position
    while (position > 0) {
        acc -= 1

        val storyStep = this[acc]

        if (storyStep?.let(isTextStory) == true) {
            return storyStep.copy(localId = GenerateId.generate()) to acc
        }
    }

    return null
}

fun Map<Int, StoryStep>.previousTextStoryPosition(
    position: Int,
    isTextStory: (StoryStep) -> Boolean
): Int? = previousTextStory(position, isTextStory)?.second

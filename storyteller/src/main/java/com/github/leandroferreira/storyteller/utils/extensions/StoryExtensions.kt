package com.github.leandroferreira.storyteller.utils.extensions

import com.github.leandroferreira.storyteller.model.story.StoryUnit

fun Map<Int, StoryUnit>.toEditState(): MutableMap<Int, List<StoryUnit>> =
    mapValues { (_, story) -> listOf(story) }.toMutableMap()

fun <T> Iterable<T>.associateWithPosition(): Map<Int, T> {
    var acc = -1

    return associateBy { ++acc }
}

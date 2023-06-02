package com.github.leandroborgesferreira.storyteller.model.story

import java.util.UUID

/**
 * The model defining the information that can be draw in the screen. This is the most basic
 * building block of the library and can have many types like image, message, audio, video,
 * button, empty space, etc.
 */
data class StoryStep(
    override val id: String = UUID.randomUUID().toString(),
    override val localId: String = UUID.randomUUID().toString(),
    override val type: String,
    override val parentId: String? = null,
    val url: String? = null,
    val path: String? = null,
    val text: String? = null,
    val title: String? = null,
    val checked: Boolean? = false,
    val steps: List<StoryUnit> = emptyList()
): StoryUnit {

    override val key: Int = localId.hashCode()

    override fun copyWithNewParent(parentId: String?): StoryUnit = copy(parentId = parentId)

    override fun copyWithNewId(id: String): StoryUnit {
        val newSteps = steps.map { storyUnit ->
            storyUnit.copyWithNewParent(id)
        }

        return copy(localId = id, steps = newSteps)
    }

    override val isGroup: Boolean = false
}

package br.com.leandroferreira.storyteller.model.story

import java.util.UUID

/**
 * A group of [StoryUnit]. This can be a image gallery, a video gallery, a list of messages, etc.
 */
data class GroupStep(
    override val id: String = UUID.randomUUID().toString(),
    override val localId: String,
    override val type: String,
    override val parentId: String? = null,
    val steps: List<StoryUnit> = emptyList()
): StoryUnit {

    override val key: Int = hashCode()

    override fun copyWithNewParent(parentId: String?): StoryUnit = copy(parentId = parentId)

    override fun copyWithNewId(id: String): StoryUnit {
        val newSteps = steps.map { storyUnit ->
            storyUnit.copyWithNewParent(id)
        }

        return copy(localId = id, steps = newSteps)
    }

    override val isGroup: Boolean = true
}

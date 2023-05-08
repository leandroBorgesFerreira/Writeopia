package br.com.leandroferreira.storyteller.model.story

/**
 * A group of [StoryUnit]. This can be a image gallery, a video gallery, a list of messages, etc.
 */
data class GroupStep(
    override val id: String,
    override val type: String,
    override val parentId: String? = null,
    val steps: List<StoryUnit>
): StoryUnit {

    override val key: Int = hashCode()

    override fun copyWithNewParent(parentId: String?): StoryUnit = copy(parentId = parentId)

    override fun copyWithNewId(id: String): StoryUnit = copy(id = id)
}

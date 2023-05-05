package br.com.leandroferreira.storyteller.model.story

/**
 * A group of [StoryUnit]. This can be a image gallery, a video gallery, a list of messages, etc.
 */
data class GroupStep(
    override val id: String,
    override val type: String,
    override val localPosition: Int,
    override val parentId: String? = null,
    val steps: List<StoryUnit>
): StoryUnit {

    override val key: Int = hashCode()

    override fun copyWithNewPosition(position: Int): StoryUnit = copy(localPosition = position)

    override fun copyWithNewParent(parentId: String?): StoryUnit = copy(parentId = parentId)
}

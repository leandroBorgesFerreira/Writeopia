package br.com.leandroferreira.storyteller.model

data class StoryStep(
    override val id: String,
    override val type: String,
    override val localPosition: Int,
    override val parentId: String? = null,
    val url: String? = null,
    val path: String? = null,
    val text: String? = null,
    val title: String? = null
): StoryUnit {

    override val key: Int = hashCode()

    override fun copyWithNewPosition(position: Int): StoryUnit = copy(localPosition = position)

    override fun copyWithNewParent(parentId: String?): StoryUnit = copy(parentId = parentId)
}

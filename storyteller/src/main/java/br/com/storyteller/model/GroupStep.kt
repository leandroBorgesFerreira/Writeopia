package br.com.storyteller.model

data class GroupStep(
    override val id: String,
    override val type: String,
    override val localPosition: Int,
    val steps: List<StoryUnit>
): StoryUnit {

    override val key: Int = hashCode()

    override fun copyWithNewPosition(position: Int): StoryUnit = copy(localPosition = position)
}

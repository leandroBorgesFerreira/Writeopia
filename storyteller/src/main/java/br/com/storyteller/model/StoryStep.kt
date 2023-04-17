package br.com.storyteller.model

data class StoryStep(
    override val id: String,
    override val type: String,
    override val localPosition: Int,
    val url: String? = null,
    val path: String? = null,
    val text: String? = null,
    val title: String? = null
): StoryUnit {

    override fun copyWithNewPosition(position: Int): StoryUnit = copy(localPosition = position)
}

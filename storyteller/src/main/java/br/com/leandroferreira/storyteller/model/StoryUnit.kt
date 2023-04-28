package br.com.leandroferreira.storyteller.model

interface StoryUnit : Comparable<StoryUnit>{
    val id: String
    val type: String
    val localPosition: Int

    val parentId: String?
    val key: Int

    fun copyWithNewPosition(position: Int): StoryUnit

    fun copyWithNewParent(parentId: String?): StoryUnit

    override fun compareTo(other: StoryUnit): Int = localPosition.compareTo(other.localPosition)
}

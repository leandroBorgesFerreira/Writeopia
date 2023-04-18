package br.com.storyteller.model

interface StoryUnit : Comparable<StoryUnit>{
    val id: String
    val type: String
    val localPosition: Int

    val key: Int

    fun copyWithNewPosition(position: Int): StoryUnit

    override fun compareTo(other: StoryUnit): Int = localPosition.compareTo(other.localPosition)
}

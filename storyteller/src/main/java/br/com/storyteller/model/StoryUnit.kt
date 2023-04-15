package br.com.storyteller.model

interface StoryUnit : Comparable<StoryUnit>{
    val id: String
    val type: String
    val localPosition: Int

    override fun compareTo(other: StoryUnit): Int = localPosition.compareTo(other.localPosition)
}

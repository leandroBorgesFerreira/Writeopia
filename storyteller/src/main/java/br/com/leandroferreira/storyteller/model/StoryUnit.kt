package br.com.leandroferreira.storyteller.model

/**
 * Interface defining the minimum behaviour for the StoryUnits that the StoryTeller library
 * needs to correctly deal with a piece of story and correctly draw and interact with it (by
 * moving, deleting, sorting...)
 */
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

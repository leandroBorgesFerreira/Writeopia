package br.com.leandroferreira.storyteller.model.story

import java.util.UUID

/**
 * Interface defining the minimum behaviour for the StoryUnits that the StoryTeller library
 * needs to correctly deal with a piece of story and correctly draw and interact with it (by
 * moving, deleting, sorting...)
 */
interface StoryUnit {
    val id: String
    val type: String

    val parentId: String?
    val key: Int

    fun copyWithNewParent(parentId: String?): StoryUnit
    fun copyWithNewId(id: String = UUID.randomUUID().toString()): StoryUnit
}

package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.manager.ContentHandler
import com.github.leandroborgesferreira.storyteller.model.story.StoryState

/**
 * Manager for
 */
interface BackstackManager : BackstackInform {

    fun previousState(state: StoryState): StoryState

    fun nextState(state: StoryState): StoryState

    companion object {
        fun create(contentHandler: ContentHandler): BackstackManager =
            PerStateBackstackManager(contentHandler = contentHandler)
    }
}
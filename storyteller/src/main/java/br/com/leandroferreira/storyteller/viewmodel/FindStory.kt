package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryUnit

object FindStory {

    fun findById(storyMap: Map<Int, StoryUnit>, storyId: String): Pair<StoryUnit?, GroupStep?>? {
        storyMap.values.forEach { storyUnit ->
            if (storyUnit.id == storyId) return storyUnit to null

            if (storyUnit is GroupStep) {
                storyUnit.steps.forEach { innerStory ->
                    if (innerStory.id == storyId) return innerStory to storyUnit
                }
            }
        }

        return null
    }
}

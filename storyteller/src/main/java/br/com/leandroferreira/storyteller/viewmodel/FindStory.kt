package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryUnit

/**
 * Helper object to find StoryUnits inside a the List<Story>. This object search in the list and
 * also in GroupSteps.
 */
object FindStory {

    /**
     * Finds the StoryUnit and its container [GroupStep], if available.
     */
    fun findById(storyMap: List<StoryUnit>, storyId: String): Pair<StoryUnit?, GroupStep?>? {
        storyMap.forEach { storyUnit ->
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

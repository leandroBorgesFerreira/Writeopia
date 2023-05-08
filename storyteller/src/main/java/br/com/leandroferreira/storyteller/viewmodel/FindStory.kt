package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit

/**
 * Helper object to find StoryUnits inside a the List<Story>. This object search in the list and
 * also in GroupSteps.
 */
object FindStory {

    /**
     * Finds the StoryUnit and its container [GroupStep], if available.
     */
    fun findById(
        storyList: Iterable<StoryUnit>,
        storyId: String
    ): Pair<StoryUnit?, GroupStep?>? {
        storyList.forEach { storyUnit ->
            if (storyUnit.id == storyId) return storyUnit to null

            if (storyUnit is GroupStep) {
                storyUnit.steps.forEach { innerStory ->
                    if (innerStory.id == storyId) return innerStory to storyUnit
                }
            }
        }

        return null
    }

    /**
     * Todo: Add unit test
     */
    fun previousFocus(storyList: List<StoryUnit>, localPosition: Int): StoryUnit? {
        for (i in localPosition downTo 0) {
            if ((storyList[i] as? StoryStep)?.text?.isNotEmpty() == true) {
                return storyList[i]
            }
        }

        return null
    }

}

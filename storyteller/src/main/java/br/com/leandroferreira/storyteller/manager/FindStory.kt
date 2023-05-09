package br.com.leandroferreira.storyteller.manager

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit

/**
 * Helper object to find StoryUnits inside a the List<Story>. This object search in the list and
 * also in GroupSteps.
 */
object FindStory {

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

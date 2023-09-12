package io.storiesteller.sdk.manager

import io.storiesteller.sdk.models.story.StoryStep

/**
 * Helper object to find StoryUnits inside a the List<Story>. This object search in the list and
 * also in GroupSteps.
 */
object FindStory {
    
    fun previousFocus(
        storyList: List<StoryStep>,
        localPosition: Int,
        focusableTypes: Set<Int>
    ): StoryStep? {
        for (i in (localPosition - 1) downTo 0) {
            if (focusableTypes.contains(storyList[i].type.number)) {
                return storyList[i]
            }
        }

        return null
    }

}

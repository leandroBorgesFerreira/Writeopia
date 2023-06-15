package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType

/**
 * This class is responsible to control the focus of the edition of story. (example: If the text
 * of a story is being edited, this story has the current focus.)
 */
class FocusHandler(
    private val isMessageFn: (String) -> Boolean = { type ->
        type == StoryType.MESSAGE.type ||
                type == StoryType.MESSAGE_BOX.type ||
                type == StoryType.CHECK_ITEM.type
    }
) {

    fun findNextFocus(position: Int, stories: Map<Int, StoryStep>): Pair<Int, StoryStep>? {
        val storyList = stories.values.toList()

        for (i in position..storyList.lastIndex) {
            val storyStep = storyList[i]
            if (isMessageFn(storyStep.type)) {
                return i to storyStep
            }
        }

        return null
    }
}

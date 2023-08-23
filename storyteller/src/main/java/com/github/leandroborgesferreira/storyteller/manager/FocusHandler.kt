package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes

/**
 * This class is responsible to control the focus of the edition of story. (example: If the text
 * of a story is being edited, this story has the current focus.)
 */
class FocusHandler(
    private val isMessageFn: (Int) -> Boolean = { typeNumber ->
        typeNumber == StoryTypes.MESSAGE.type.number ||
                typeNumber == StoryTypes.MESSAGE_BOX.type.number ||
                typeNumber == StoryTypes.CHECK_ITEM.type.number
    }
) {

    fun findNextFocus(position: Int, stories: Map<Int, StoryStep>): Pair<Int, StoryStep>? {
        val storyList = stories.values.toList()

        for (i in position..storyList.lastIndex) {
            val storyStep = storyList[i]
            if (isMessageFn(storyStep.type.number)) {
                return i to storyStep
            }
        }

        return null
    }
}

package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType

class FocusHandler(
    private val isMessageFn: (String) -> Boolean = { type ->
        type == StoryType.MESSAGE.type ||
                type == StoryType.MESSAGE_BOX.type ||
                type == StoryType.CHECK_ITEM.type
    }
) {

    fun findNextFocus(position: Int, stories: Map<Int, StoryStep>): String? {
        val storyList = stories.values.toList()

        return storyList.subList(position, storyList.lastIndex)
            .firstOrNull { storyStep -> isMessageFn(storyStep.type) }
            ?.id
    }
}
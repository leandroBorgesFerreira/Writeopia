package io.writeopia.sdk.manager

import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

/**
 * This class is responsible to control the focus of the edition of story. (example: If the text
 * of a story is being edited, this story has the current focus.)
 */
class FocusHandler(
    private val isMessageFn: (Int) -> Boolean = { typeNumber ->
        typeNumber == StoryTypes.TEXT.type.number ||
            typeNumber == StoryTypes.TEXT_BOX.type.number ||
            typeNumber == StoryTypes.CHECK_ITEM.type.number ||
            typeNumber == StoryTypes.UNORDERED_LIST_ITEM.type.number
    }
) {

    fun findNextFocus(position: Int, stories: Map<Int, StoryStep>): Pair<Int, StoryStep>? {
        for (i in position..stories.keys.last()) {
            val storyStep = stories[i]
            if (storyStep != null && isMessageFn(storyStep.type.number)) {
                return i to storyStep.copy(localId = GenerateId.generate())
            }
        }

        return null
    }
}

package io.writeopia.ui.backstack

import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.story.StoryStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val DEFAULT_TEXT_EDIT_LIMIT = 20

class SnapshotBackstackManager(
    private val textEditLimit: Int = DEFAULT_TEXT_EDIT_LIMIT,
) : BackstackInform {

    private var lastEditPosition: Int = -1
    private var textEditCount = 0

    private val _canUndo = MutableStateFlow(false)
    private val _canRedo = MutableStateFlow(false)

    override val canUndo: StateFlow<Boolean> = _canUndo
    override val canRedo: StateFlow<Boolean> = _canRedo

    private val stateList: MutableList<StoryState> = mutableListOf()

    fun previousState(): StoryState {
        TODO("Not yet implemented")
    }

    fun nextState(): StoryState {
        TODO("Not yet implemented")
    }

    fun addState(state: StoryState) {
        TODO("Not yet implemented")
    }

    fun addTextState(
        storyStep: StoryStep,
        position: Int
    ) {

        if (position == lastEditPosition) {
            if (textEditCount < textEditLimit) {
                textEditCount++
            } else {
                lastEditPosition = position
                textEditCount = 1
//                backStack.add(action)
            }
        } else {
            lastEditPosition = position
            textEditCount = 1
//            backStack.add(action)
        }
    }

    /**
     * This is a hash code that contains information that don't change very often, like localId.
     */
    private fun StoryStep.persistentHashcode(): Int {
        return listOf(
            id,
            type,
            parentId,
            url,
            path,
            text,
            checked,
            steps,
            tags,
            spans,
            decoration,
            ephemeral,
            documentLink
        ).hashCode()
    }

}

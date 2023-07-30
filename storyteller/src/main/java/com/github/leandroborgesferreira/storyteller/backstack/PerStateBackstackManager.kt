package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.manager.ContentHandler
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction
import com.github.leandroborgesferreira.storyteller.model.action.SingleAction
import com.github.leandroborgesferreira.storyteller.model.story.LastEdit
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Stack

private const val DEFAULT_TEXT_EDIT_LIMIT = 20

internal class PerStateBackstackManager(
    //A dynamic value would be better!
    private val textEditLimit: Int = DEFAULT_TEXT_EDIT_LIMIT,
    private val contentHandler: ContentHandler
) : BackstackManager {

    private var lastEditPosition: Int = -1
    private var textEditCount = 0

    private val backStack: Stack<BackstackAction> = Stack()
    private val forwardStack: Stack<BackstackAction> = Stack()

    private val _canUndo = MutableStateFlow(false)
    private val _canRedo = MutableStateFlow(false)

    override val canUndo: StateFlow<Boolean> = _canUndo
    override val canRedo: StateFlow<Boolean> = _canRedo

    override fun previousState(state: StoryState): StoryState {
        //Todo: Change all the -> state
        return when (val action = previousAction()) {
            is BackstackAction.BulkDelete -> state
            is BackstackAction.Delete -> revertDelete(state, action)
            is BackstackAction.Merge -> state
            is BackstackAction.Move -> state
            is BackstackAction.StoryStateChange -> revertStoryState(state, action)
            is BackstackAction.StoryTextChange -> revertStoryState(state, action)
            is BackstackAction.Add -> state
        }
    }

    override fun nextState(state: StoryState): StoryState {
        nextAction()
        return StoryState(stories = emptyMap(), LastEdit.Nothing)
    }

    internal fun peek(): BackstackAction = backStack.peek()

    override fun addAction(action: BackstackAction) {
        forwardStack.clear()

        when {
            action is BackstackAction.StoryTextChange -> {
                addTextState(action)
            }

            backStack.isEmpty() -> {
                addState(action)
            }

            action is BackstackAction.StoryStateChange -> {
                addState(action)
            }

            else -> {
                addState(action)
            }
        }
    }

    private fun previousAction(): BackstackAction = backStack.pop().also { action ->
        forwardStack.addAndNotify(action)
    }

    private fun nextAction(): BackstackAction = forwardStack.pop().also { action ->
        backStack.addAndNotify(action)
    }

    private fun <T> Stack<T>.addAndNotify(element: T) {
        add(element)
        _canUndo.value = backStack.isNotEmpty()
        _canRedo.value = forwardStack.isNotEmpty()
    }

    private fun addState(action: BackstackAction) {
        backStack.addAndNotify(action)
    }

    private fun revertStoryState(state: StoryState, action: SingleAction): StoryState {
        val stories = state.stories.toMutableMap()
        val position = action.position
        val storyStep = action.storyStep

        stories[position] = storyStep.copyNewLocalId()

        return StoryState(
            stories,
            LastEdit.LineEdition(position, storyStep),
            focusId = storyStep.id
        )
    }

    private fun addTextState(action: BackstackAction.StoryTextChange) {
        val position = action.position

        if (position == lastEditPosition) {
            if (textEditCount < textEditLimit) {
                textEditCount++
            } else {
                lastEditPosition = position
                textEditCount = 1
                backStack.addAndNotify(action)
            }
        } else {
            lastEditPosition = position
            textEditCount = 1
            backStack.addAndNotify(action)
        }
    }

    private fun revertDelete(storyState: StoryState, delete: BackstackAction.Delete): StoryState {
        val newStory = contentHandler.addNewContent(
            storyState.stories,
            delete.storyStep,
            delete.position
        )

        return StoryState(
            stories = newStory,
            lastEdit = LastEdit.Whole,
            focusId = delete.storyStep.id
        )
    }

}
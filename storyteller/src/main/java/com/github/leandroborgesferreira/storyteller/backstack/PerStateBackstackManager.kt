package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.manager.ContentHandler
import com.github.leandroborgesferreira.storyteller.manager.MovementHandler
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction
import com.github.leandroborgesferreira.storyteller.model.action.SingleAction
import com.github.leandroborgesferreira.storyteller.model.story.LastEdit
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Stack

private const val DEFAULT_TEXT_EDIT_LIMIT = 20

internal class PerStateBackstackManager(
    //A dynamic value would be better!
    private val textEditLimit: Int = DEFAULT_TEXT_EDIT_LIMIT,
    private val contentHandler: ContentHandler,
    private val movementHandler: MovementHandler = MovementHandler()
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
        return when (val action = previousAction()) {
            is BackstackAction.BulkDelete -> {
                forwardStack.add(action)
                revertBulkDelete(state, action)
            }

            is BackstackAction.Delete -> {
                forwardStack.add(action)
                revertDelete(state, action.storyStep, action.position)
            }

            is BackstackAction.Merge -> state //Todo
            is BackstackAction.Move -> {
                forwardStack.add(action)

                val newStories = movementHandler.move(state.stories, action.revertMove())

                StoryState(
                    stories = newStories,
                    lastEdit = LastEdit.Whole,
                    focusId = action.storyStep.id
                )
            }

            is BackstackAction.StoryStateChange -> {
                forwardStack.keepState(state, action)
                revertStoryState(state, action)
            }

            is BackstackAction.StoryTextChange -> {
                forwardStack.keepState(state, action)
                revertStoryState(state, action)
            }

            is BackstackAction.Add -> {
                forwardStack.add(action)
                revertAddStory(state, action.storyStep, action.position)
            }
        }.also {
            _canRedo.value = forwardStack.isNotEmpty()
            _canUndo.value = backStack.isNotEmpty()
        }
    }

    override fun nextState(state: StoryState): StoryState {
        return when (val action = nextAction()) {
            is BackstackAction.BulkDelete -> {
                val (newStories, _) = contentHandler.bulkDeletion(
                    action.deletedUnits.keys,
                    state.stories
                )

                backStack.add(action)

                StoryState(newStories, LastEdit.Whole)
            }

            is BackstackAction.Delete -> {
                backStack.add(action)
                revertAddStory(state, action.storyStep, action.position)
            }

            is BackstackAction.Merge -> state
            is BackstackAction.Move -> state
            is BackstackAction.StoryStateChange -> {
                backStack.keepState(state, action)
                revertStoryState(state, action)
            }

            is BackstackAction.StoryTextChange -> {
                backStack.keepState(state, action)
                revertStoryState(state, action)
            }

            is BackstackAction.Add -> {
                backStack.add(action)
                revertDelete(state, action.storyStep, action.position)
            }
        }.also {
            _canRedo.value = forwardStack.isNotEmpty()
            _canUndo.value = backStack.isNotEmpty()
        }
    }

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

        _canRedo.value = forwardStack.isNotEmpty()
        _canUndo.value = backStack.isNotEmpty()
    }

    internal fun peek(): BackstackAction = backStack.peek()

    private fun Stack<BackstackAction>.keepState(
        state: StoryState,
        action: SingleAction,
    ) {
        state.stories[action.position]?.let { storyStep ->
            this.add(
                BackstackAction.StoryStateChange(
                    storyStep,
                    action.position
                )
            )
        }
    }

    private fun previousAction(): BackstackAction = backStack.pop()

    private fun nextAction(): BackstackAction = forwardStack.pop()

    private fun addState(action: BackstackAction) {
        backStack.add(action)
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
                backStack.add(action)
            }
        } else {
            lastEditPosition = position
            textEditCount = 1
            backStack.add(action)
        }
    }

    private fun revertDelete(
        storyState: StoryState,
        storyStep: StoryStep,
        position: Int
    ): StoryState {
        val newStory = contentHandler.addNewContent(
            storyState.stories,
            storyStep,
            position
        )

        return StoryState(
            stories = newStory,
            lastEdit = LastEdit.Whole,
            focusId = storyStep.id
        )
    }

    private fun revertBulkDelete(storyState: StoryState, action: BackstackAction.BulkDelete) =
        contentHandler.addNewContentBulk(storyState.stories,
            action.deletedUnits,
            addInBetween = {
                StoryStep(type = StoryType.SPACE.type)
            }
        ).let { newStories ->
            StoryState(
                newStories,
                lastEdit = LastEdit.Whole
            )
        }

    private fun revertAddStory(
        storyState: StoryState,
        storyStep: StoryStep,
        position: Int
    ): StoryState =
        contentHandler.deleteStory(
            Action.DeleteStory(storyStep, position = position),
            storyState.stories
        ) ?: storyState

    private fun BackstackAction.Move.revertMove(): Action.Move =
        Action.Move(
            storyStep = storyStep,
            positionFrom = positionTo,
            positionTo = maxOf(positionFrom - 1, 0)
        )
}
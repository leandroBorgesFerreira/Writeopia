package io.writeopia.sdk.backstack

import io.writeopia.sdk.manager.ContentHandler
import io.writeopia.sdk.manager.MovementHandler
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.action.BackstackAction
import io.writeopia.sdk.model.action.SingleAction
import io.writeopia.sdk.model.story.LastEdit
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val DEFAULT_TEXT_EDIT_LIMIT = 20

/**
 * This is the default implementation of [BackstackManager]. It checks the previous or next state
 * and react accordingly. This class doesn't perform and state change and instead delegates this
 * function to its parameters. The oly responsibility of this class is to coordinate the state of
 * the backstack.
 *
 * @param textEditLimit The limit of the text that should be used to divide two backstack calls.
 * this is important to avoid an undo call to erase too much text.
 * @param contentHandler [ContentHandler] Used to add/delete content.
 * @param movementHandler [MovementHandler] Used to move, merge and separate content.
 */
internal class PerStateBackstackManager(
    private val textEditLimit: Int = DEFAULT_TEXT_EDIT_LIMIT,
    private val contentHandler: ContentHandler,
    private val movementHandler: MovementHandler
) : BackstackManager {

    private var lastEditPosition: Int = -1
    private var textEditCount = 0

    private val backStack: MutableList<BackstackAction> = mutableListOf()
    private val forwardStack: MutableList<BackstackAction> = mutableListOf()

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
                )
            }

            is BackstackAction.StoryStateChange -> {
                forwardStack.keepState(state, action)
                revertStoryState(state, action, action.storyStep.id)
            }

            is BackstackAction.StoryTextChange -> {
                forwardStack.keepState(state, action)
                revertStoryState(state, action, action.storyStep.id)
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

    override fun nextState(state: StoryState): StoryState =
        when (val action = nextAction()) {
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

            is BackstackAction.Move -> {
                backStack.add(action)

                val newStory = movementHandler.move(
                    state.stories,
                    Action.Move(action.storyStep, action.positionFrom, action.positionTo)
                )
                StoryState(
                    newStory,
                    lastEdit = LastEdit.Whole
                )
            }

            is BackstackAction.StoryStateChange -> {
                backStack.keepState(state, action)
                revertStoryState(state, action, action.storyStep.id)
            }

            is BackstackAction.StoryTextChange -> {
                backStack.keepState(state, action)
                revertStoryState(state, action, action.storyStep.id)
            }

            is BackstackAction.Add -> {
                backStack.add(action)
                revertDelete(state, action.storyStep, action.position)
            }
        }.also {
            _canRedo.value = forwardStack.isNotEmpty()
            _canUndo.value = backStack.isNotEmpty()
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

    internal fun peek(): BackstackAction = backStack.last()

    private fun MutableList<BackstackAction>.keepState(
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

    private fun previousAction(): BackstackAction = backStack.removeLast()

    private fun nextAction(): BackstackAction = forwardStack.removeLast()

    private fun addState(action: BackstackAction) {
        backStack.add(action)
    }

    private fun revertStoryState(state: StoryState, action: SingleAction, focusId: String?): StoryState {
        val stories = state.stories.toMutableMap()
        val position = action.position
        val storyStep = action.storyStep

        stories[position] = storyStep.copyNewLocalId()

        return StoryState(
            stories,
            LastEdit.LineEdition(position, storyStep),
            focusId
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
        )
    }

    private fun revertBulkDelete(storyState: StoryState, action: BackstackAction.BulkDelete) =
        contentHandler.addNewContentBulk(storyState.stories,
            action.deletedUnits,
            addInBetween = {
                StoryStep(type = StoryTypes.SPACE.type)
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
            positionFrom = positionTo - 1,
            positionTo = maxOf(positionFrom - 1, 0)
        )
}
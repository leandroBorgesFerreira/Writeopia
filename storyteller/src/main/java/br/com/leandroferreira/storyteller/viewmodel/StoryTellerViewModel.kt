package br.com.leandroferreira.storyteller.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.model.Command
import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryState
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.StepsNormalizationBuilder
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.viewmodel.move.MoveHandler
import br.com.leandroferreira.storyteller.viewmodel.move.SpaceMoveHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StoryTellerViewModel(
    private val storiesRepository: StoriesRepository,
    private val stepsNormalizer: (Iterable<StoryUnit>) -> List<StoryUnit> =
        StepsNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val moveHandler: MoveHandler = SpaceMoveHandler(),
) : ViewModel() {

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _normalizedSteps: MutableStateFlow<StoryState> = MutableStateFlow(
        StoryState(stories = emptyList())
    )
    val normalizedStepsState: StateFlow<StoryState> = _normalizedSteps.asStateFlow()

    private val _scrollToPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val scrollToPosition: StateFlow<Int?> = _scrollToPosition.asStateFlow()

    fun requestHistoriesFromApi(force: Boolean = false) {
        if (_normalizedSteps.value.stories.isEmpty() || force) {
            viewModelScope.launch {
                val normalizedSteps = stepsNormalizer(storiesRepository.history())
                _normalizedSteps.value = StoryState(normalizedSteps)
            }
        }
    }

    fun mergeRequest(receiverId: String, senderId: String) {
        val sender = FindStory.findById(_normalizedSteps.value.stories, senderId)?.first
        FindStory.findById(_normalizedSteps.value.stories, receiverId)
            ?.let { (receiver, parentReceiver) ->
                when {
                    sender != null && parentReceiver != null -> {
                        val position = parentReceiver.localPosition
                        _normalizedSteps.value =
                            StoryState(
                                stories = changePositionOfStep(sender, position),
                                focusId = parentReceiver.id
                            )
                    }

                    sender != null && receiver != null -> {
                        val position = receiver.localPosition
                        _normalizedSteps.value =
                            StoryState(
                                stories = changePositionOfStep(sender, position),
                                focusId = receiver.id
                            )
                    }
                }
            }
    }

    private fun changePositionOfStep(sender: StoryUnit, position: Int): List<StoryUnit> {
        val mutableHistory = _normalizedSteps.value.stories.toMutableList()
        mutableHistory[sender.localPosition] = sender.copyWithNewPosition(position)

        return stepsNormalizer(mutableHistory)
    }

    fun moveRequest(unitId: String, newPosition: Int) {
        _normalizedSteps.value = StoryState(
            moveHandler.handleMove(
                _normalizedSteps.value.stories,
                unitId,
                newPosition
            ).let(stepsNormalizer)
        )

        _scrollToPosition.value =
            FindStory.findById(_normalizedSteps.value.stories, unitId)
                ?.let { (unit, group) ->
                    group?.localPosition ?: unit?.localPosition
                }
    }

    fun checkRequest(unitId: String, checked: Boolean) {
        updateState()

        FindStory.findById(_normalizedSteps.value.stories, unitId)
            ?.first
            ?.let { storyUnit -> storyUnit as? StoryStep }
            ?.let { step ->
                val newStep = step.copy(checked = checked)
                val newList = _normalizedSteps.value.stories.toMutableList()

                newList[newStep.localPosition] = newStep

                _normalizedSteps.value = StoryState(newList, null)
            }
    }

    fun onListCommand(command: Command) {
        when (command.type) {
            "move_up" -> {
                updateState()
                _normalizedSteps.value = moveUp(
                    command.step.localPosition,
                    _normalizedSteps.value.stories
                )
            }

            "move_down" -> {
                updateState()
                _normalizedSteps.value = moveDown(
                    command.step.localPosition,
                    _normalizedSteps.value.stories
                )
            }

            "delete" -> {
                onDelete(command.step)
            }
        }
    }

    fun onTextEdit(text: String, position: Int) {
        textChanges[position] = text
    }

    fun onSimpleTextEdit(text: String, position: Int) {
        textChanges[position] = text

        if (text.contains("\n")) {
            updateState()
        }
    }

    fun updateState() {
        updateTexts()
    }

    private fun updateTexts() {
        val steps = _normalizedSteps.value.stories.toMutableList()

        textChanges.forEach { (position, text) ->
            val editStep = steps[position]

            (editStep as? StoryStep)?.copy(text = text)?.let { step ->
                steps[position] = step
            }
        }

        textChanges.clear()

        _normalizedSteps.value = StoryState(steps)
    }

    private fun moveUp(
        position: Int,
        history: List<StoryUnit>,
    ): StoryState {
        val thisStep = history[position]
        val upStep = history[position - 1]

        val mutableHistory = history.toMutableList()
        mutableHistory[position] = upStep.copyWithNewPosition(position)

        mutableHistory[position - 1] = thisStep.copyWithNewPosition(position - 1)

        return StoryState(stepsNormalizer(mutableHistory))
    }

    private fun moveDown(
        position: Int,
        history: List<StoryUnit>,
    ): StoryState = moveUp(position + 1, history)

    fun onDelete(step: StoryUnit) {
        updateState()
        delete(step, _normalizedSteps.value.stories)
    }

    private fun delete(
        step: StoryUnit,
        history: List<StoryUnit>,
    ) {
        val parentId = step.parentId
        val mutableSteps = _normalizedSteps.value.stories.toMutableList()

        if (parentId == null) {
            mutableSteps.removeAt(step.localPosition)
            val normalized = stepsNormalizer(mutableSteps)
            val previousFocus = FindStory.previousFocus(normalized, step.localPosition)

            _normalizedSteps.value = StoryState(normalized, focusId = previousFocus?.id)
        } else {
            FindStory.findById(history, parentId)
                ?.first
                ?.let { group ->
                    val newSteps = (group as GroupStep).steps.filter { storyUnit ->
                        storyUnit.id != step.id
                    }

                    val newStoryUnit = if (newSteps.size == 1) {
                        newSteps.first().copyWithNewPosition(group.localPosition)
                    } else {
                        group.copy(steps = newSteps)
                    }

                    mutableSteps[group.localPosition] = newStoryUnit.copyWithNewParent(null)
                    _normalizedSteps.value = StoryState(stepsNormalizer(mutableSteps))
                }
        }
    }
}

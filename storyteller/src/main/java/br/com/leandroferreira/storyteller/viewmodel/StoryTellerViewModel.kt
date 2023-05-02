package br.com.leandroferreira.storyteller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.model.Command
import br.com.leandroferreira.storyteller.model.GroupStep
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

//Todo: This class it associating to many times. The performance must be improved.
class StoryTellerViewModel(
    private val storiesRepository: StoriesRepository,
    private val stepsNormalizer: (Iterable<StoryUnit>) -> List<StoryUnit> =
        StepsNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val moveHandler: MoveHandler = SpaceMoveHandler(),
) : ViewModel() {

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _normalizedSteps: MutableStateFlow<List<StoryUnit>> = MutableStateFlow(emptyList())
    val normalizedStepsState: StateFlow<List<StoryUnit>> = _normalizedSteps.asStateFlow()

    fun requestHistoriesFromApi(force: Boolean = false) {
        if (_normalizedSteps.value.isEmpty() || force) {
            viewModelScope.launch {
                val normalizedSteps = stepsNormalizer(storiesRepository.history())
                _normalizedSteps.value = normalizedSteps
            }
        }
    }


    //Todo: Review the performance of this method later
    fun mergeRequest(receiverId: String, senderId: String) {
        val sender = FindStory.findById(_normalizedSteps.value, senderId)?.first
        val receiver = FindStory.findById(_normalizedSteps.value, receiverId)?.first

        if (sender != null && receiver != null) {
            val mutableHistory = _normalizedSteps.value.toMutableList()
            mutableHistory[sender.localPosition] =
                sender.copyWithNewPosition(receiver.localPosition)

            _normalizedSteps.value = stepsNormalizer(mutableHistory)
        }
    }

    fun moveRequest(unitId: String, newPosition: Int) {
        val result = moveHandler.handleMove(
            _normalizedSteps.value,
            unitId,
            newPosition
        )

        _normalizedSteps.value = stepsNormalizer(result)
    }

    fun onListCommand(command: Command) {
        when (command.type) {
            "move_up" -> {
                updateTexts()
                _normalizedSteps.value = moveUp(command.step.localPosition, _normalizedSteps.value)
            }

            "move_down" -> {
                updateTexts()
                _normalizedSteps.value =
                    moveDown(command.step.localPosition, _normalizedSteps.value)
            }

            "delete" -> {
                updateTexts()

                delete(command.step, _normalizedSteps.value)
            }
        }
    }

    fun onTextEdit(text: String, position: Int) {
        textChanges[position] = text
    }

    fun updateState() {
        updateTexts()
    }

    private fun updateTexts() {
        val steps = _normalizedSteps.value.toMutableList()

        textChanges.forEach { (position, text) ->
            val editStep = steps[position]

            (editStep as? StoryStep)?.copy(text = text)?.let { step ->
                steps[position] = step
            }
        }

        _normalizedSteps.value = steps
    }

    private fun moveUp(
        position: Int,
        history: List<StoryUnit>,
    ): List<StoryUnit> {
        val thisStep = history[position]
        val upStep = history[position - 1]

        val mutableHistory = history.toMutableList()
        mutableHistory[position] = upStep.copyWithNewPosition(position)

        mutableHistory[position - 1] = thisStep.copyWithNewPosition(position - 1)

        return stepsNormalizer(mutableHistory)
    }

    private fun moveDown(
        position: Int,
        history: List<StoryUnit>,
    ): List<StoryUnit> = moveUp(position + 1, history)

    private fun delete(
        step: StoryUnit,
        history: List<StoryUnit>,
    ) {
        val parentId = step.parentId
        val mutableSteps = _normalizedSteps.value.toMutableList()

        if (parentId == null) {
            mutableSteps.removeAt(step.localPosition)
            _normalizedSteps.value = stepsNormalizer(mutableSteps)
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
                    _normalizedSteps.value = stepsNormalizer(mutableSteps)
                }
        }
    }
}

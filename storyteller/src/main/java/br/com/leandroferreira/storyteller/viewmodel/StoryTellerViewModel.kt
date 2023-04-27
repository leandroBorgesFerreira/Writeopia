package br.com.leandroferreira.storyteller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.model.Command
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.StepsNormalizationBuilder
import br.com.leandroferreira.storyteller.normalization.addinbetween.AddInBetween
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.viewmodel.move.MoveHandler
import br.com.leandroferreira.storyteller.viewmodel.move.SpaceMoveHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StoryTellerViewModel(
    private val storiesRepository: StoriesRepository,
    private val stepsNormalizer: (List<StoryUnit>) -> List<StoryUnit> =
        StepsNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val moveHandler: MoveHandler = SpaceMoveHandler(),
    private val spacesNormalizer: (List<StoryUnit>) -> List<StoryUnit> =
        AddInBetween.spaces()::insert
) : ViewModel() {

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _normalizedSteps: MutableStateFlow<Map<Int, StoryUnit>> = MutableStateFlow(
        emptyMap()
    )
    val normalizedStepsState: StateFlow<Map<Int, StoryUnit>> = _normalizedSteps.asStateFlow()

    fun requestHistoriesFromApi(force: Boolean = false) {
        if (_normalizedSteps.value.isEmpty() || force) {
            viewModelScope.launch {
                _normalizedSteps.value =
                    spacesNormalizer(stepsNormalizer(storiesRepository.history()))
                        .associateBy { story -> story.localPosition }
            }
        }
    }


    //Todo: Review the performance of this method later
    fun mergeRequest(receiverId: String, senderId: String) {
        val sender = FindStory.findById(_normalizedSteps.value, senderId)?.first
        val receiver = FindStory.findById(_normalizedSteps.value, receiverId)?.first

        if (sender != null && receiver != null) {
            val mutableHistory = _normalizedSteps.value.toMutableMap()
            mutableHistory[sender.localPosition] =
                sender.copyWithNewPosition(receiver.localPosition)

            val normalized = stepsNormalizer(mutableHistory.values.toList())
                .associateBy { story -> story.localPosition }

            _normalizedSteps.value = normalized
        }
    }

    fun moveRequest(unitId: String, newPosition: Int) {
        val result = moveHandler.handleMove(
            _normalizedSteps.value.toMutableMap(),
            unitId,
            newPosition
        )

        //Todo: Review performance
        _normalizedSteps.value =
            spacesNormalizer(result.values.toList()).associateBy { it.localPosition }
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
                _normalizedSteps.value = _normalizedSteps.value - command.step.localPosition
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
        val steps = _normalizedSteps.value.toMutableMap()

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
        history: Map<Int, StoryUnit>,
    ): Map<Int, StoryUnit> {
        val thisStep = history[position]
        val upStep = history[position - 1]

        val mutableHistory = history.toMutableMap()
        upStep?.let { step ->
            mutableHistory[position] = step.copyWithNewPosition(position)
        }

        thisStep?.let { step ->
            mutableHistory[position - 1] = step.copyWithNewPosition(position - 1)
        }

        return mutableHistory.values
            .toList()
            .let(stepsNormalizer)
            .associateBy { it.localPosition }
    }

    private fun moveDown(
        position: Int,
        history: Map<Int, StoryUnit>,
    ): Map<Int, StoryUnit> = moveUp(position + 1, history)

}

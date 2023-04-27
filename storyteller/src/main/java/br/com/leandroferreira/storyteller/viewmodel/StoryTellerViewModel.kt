package br.com.leandroferreira.storyteller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.model.Command
import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.StepsNormalizationBuilder
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryTellerViewModel(
    private val storiesRepository: StoriesRepository,
    private val stepsNormalizer: (List<StoryUnit>) -> List<StoryUnit> =
        StepsNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }
) : ViewModel() {

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _normalizedSteps: MutableStateFlow<Map<Int, StoryUnit>> = MutableStateFlow(
        emptyMap()
    )
    val normalizedStepsState: StateFlow<Map<Int, StoryUnit>> = _normalizedSteps

    fun requestHistoriesFromApi(force: Boolean = false) {
        if (_normalizedSteps.value.isEmpty() || force) {
            viewModelScope.launch {
                _normalizedSteps.value = stepsNormalizer(storiesRepository.history())
                    .associateBy { story -> story.localPosition }
            }
        }
    }


    //Todo: Review the performance of this method later
    fun mergeRequest(receiverId: String, senderId: String) {
        val sender = _normalizedSteps.value
            .values
            .find { storyUnit -> storyUnit.id == senderId ||
                (storyUnit as? GroupStep)?.steps?.any { innerSteps ->
                    innerSteps.id == senderId
                } == true}

        val receiver = _normalizedSteps.value
            .values
            .find { storyUnit ->
                storyUnit.id == receiverId ||
                    (storyUnit as? GroupStep)?.steps?.any { innerSteps ->
                        innerSteps.id == receiverId
                    } == true
            }

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
        val unitToMove = _normalizedSteps.value
            .values
            .find { storyUnit -> storyUnit.id == unitId ||
                (storyUnit as? GroupStep)?.steps?.any { innerSteps ->
                    innerSteps.id == unitId
                } == true}

        val mutableSteps = _normalizedSteps.value.toMutableMap()



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
            mutableHistory[position - 1] =
                step.copyWithNewPosition(position - 1)
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

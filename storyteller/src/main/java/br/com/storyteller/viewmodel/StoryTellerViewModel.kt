package br.com.storyteller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.storyteller.model.Command
import br.com.storyteller.model.StoryUnit
import br.com.storyteller.normalization.StepsNormalizationBuilder
import br.com.storyteller.repository.StoriesRepository
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

    private val _normalizedSteps: MutableStateFlow<Map<Int, StoryUnit>> = MutableStateFlow(
        emptyMap()
    )
    val normalizedStepsState: StateFlow<Map<Int, StoryUnit>> = _normalizedSteps

    fun requestHistoriesFromApi() {
        viewModelScope.launch {
            _normalizedSteps.value = stepsNormalizer(storiesRepository.history())
                .associateBy { story -> story.localPosition }
        }
    }

    fun handleCommand(command: Command) {
        when (command.type) {
            "move_up" -> {
                _normalizedSteps.value = moveUp(command.step.localPosition, _normalizedSteps.value)
            }

            "move_down" -> {
                _normalizedSteps.value =
                    moveDown(command.step.localPosition, _normalizedSteps.value)
            }

            "delete" -> {
                _normalizedSteps.value = _normalizedSteps.value - command.step.localPosition
            }
        }
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

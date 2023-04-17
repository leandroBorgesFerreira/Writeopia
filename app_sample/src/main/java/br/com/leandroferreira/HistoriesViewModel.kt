package br.com.leandroferreira

import android.content.Context
import br.com.storyteller.model.StoryUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoriesViewModel(private val stepsNormalizer: (List<StoryUnit>) -> List<StoryUnit>) {

    private val _editModeState = MutableStateFlow(false)
    val editModeState: StateFlow<Boolean> = _editModeState

    fun normalizedHistories(context: Context): Map<Int, StoryUnit> =
        history(context)
            .let(stepsNormalizer)
            .associateBy { it.localPosition }

    fun normalizeHistories(histories: List<StoryUnit>): List<StoryUnit> = stepsNormalizer(histories)

    fun toggleEdit() {
        _editModeState.value = !_editModeState.value
    }
}

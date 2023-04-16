package br.com.leandroferreira

import android.content.Context
import br.com.storyteller.model.StoryUnit

class HistoriesViewModel(private val stepsNormalizer: (List<StoryUnit>) -> List<StoryUnit>) {

    fun normalizedHistories(context: Context): Map<Int, StoryUnit> =
        history(context)
            .let(stepsNormalizer)
            .associateBy { it.localPosition }

    fun normalizeHistories(histories: List<StoryUnit>): List<StoryUnit> = stepsNormalizer(histories)
}

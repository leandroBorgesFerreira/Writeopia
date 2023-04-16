package br.com.storyteller.normalization.position

import br.com.storyteller.model.StoryUnit

object PositionNormalization {

    fun normalizePosition(storySteps: List<StoryUnit>): List<StoryUnit> =
        storySteps.mapIndexed { index, storyUnit  ->
            storyUnit.copyWithNewPosition(index)
        }
}

package br.com.storyteller.normalization

import br.com.storyteller.model.StoryUnit
import br.com.storyteller.normalization.buttons.ButtonsNormalization
import br.com.storyteller.normalization.merge.MergeNormalization
import br.com.storyteller.normalization.merge.StepsMergerCoordinator
import br.com.storyteller.normalization.position.PositionNormalization

class StepsNormalizationBuilder {

    companion object {
        fun reduceNormalizations(
            buildFunc: StepsNormalizationBuilder.() -> Unit
        ): (List<StoryUnit>) -> List<StoryUnit> =
            StepsNormalizationBuilder().apply(buildFunc).build()
    }

    private val normalizations: MutableList<(List<StoryUnit>) -> List<StoryUnit>> = mutableListOf()

    fun addNormalization(
        normalization: (List<StoryUnit>) -> List<StoryUnit>
    ): StepsNormalizationBuilder = apply {
        normalizations.add(normalization)
    }

    fun defaultNormalizers() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
            addMerger(StepsMergerCoordinator(typeOfStep = "message", typeOfGroup = null))
        }

        addNormalization(mergeNormalization::mergeSteps)
        addNormalization(PositionNormalization::normalizePosition)
    }

    private fun build(): (List<StoryUnit>) -> List<StoryUnit> = reduceNormalizations(normalizations)

    private fun reduceNormalizations(
        normalizations: List<(List<StoryUnit>) -> List<StoryUnit>>
    ): (List<StoryUnit>) -> List<StoryUnit> =
        normalizations.reduce { fn, gn ->
            return { gn(fn(it)) }
        }
}

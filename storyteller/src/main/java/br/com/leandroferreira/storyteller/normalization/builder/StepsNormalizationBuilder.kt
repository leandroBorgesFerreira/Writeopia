package br.com.leandroferreira.storyteller.normalization.builder

import br.com.leandroferreira.storyteller.normalization.addinbetween.AddInBetween
import br.com.leandroferreira.storyteller.normalization.merge.MergeLogic
import br.com.leandroferreira.storyteller.normalization.merge.MergeNormalization
import br.com.leandroferreira.storyteller.normalization.merge.StepsMergerCoordinator
import br.com.leandroferreira.storyteller.normalization.merge.steps.StepToStepMerger
import br.com.leandroferreira.storyteller.normalization.position.PositionNormalization
import br.com.leandroferreira.storyteller.normalization.sort.SortNormalization
import br.com.leandroferreira.storyteller.utils.UnitsNormalization

/**
 * This builder reduces the normalizers into a single function for simplification of use of
 * Normalizers
 */
class StepsNormalizationBuilder {

    companion object {
        fun reduceNormalizations(
            buildFunc: StepsNormalizationBuilder.() -> Unit
        ): UnitsNormalization =
            StepsNormalizationBuilder().apply(buildFunc).build()
    }

    private val normalizations: MutableList<UnitsNormalization> = mutableListOf()

    /**
     * Adds a normalization.
     */
    fun addNormalization(
        normalization: UnitsNormalization
    ): StepsNormalizationBuilder = apply {
        normalizations.add(normalization)
    }

    /**
     * Provides some useful normalizers. Use this to get a plug and play experience.
     */
    fun defaultNormalizers() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
            addMerger(
                StepsMergerCoordinator(
                    stepMerger = StepToStepMerger(),
                    typeOfStep = "message",
                    typeOfGroup = null,
                    mergeLogic = MergeLogic::lazy
                )
            )
        }

        normalizations.add(SortNormalization::sort)
        normalizations.add(mergeNormalization::mergeSteps)
        normalizations.add(AddInBetween.spaces()::insert)
        normalizations.add(PositionNormalization::normalizePosition)
    }

    private fun build(): UnitsNormalization = reduceNormalizations(normalizations)

    private fun reduceNormalizations(
        normalizations: Iterable<UnitsNormalization>
    ): UnitsNormalization =
        normalizations.reduce { fn, gn -> { stories -> gn(fn(stories)) } }
}
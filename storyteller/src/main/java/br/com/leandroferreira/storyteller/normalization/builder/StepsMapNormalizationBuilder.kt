package br.com.leandroferreira.storyteller.normalization.builder

import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.addinbetween.AddInBetween
import br.com.leandroferreira.storyteller.normalization.merge.MergeLogic
import br.com.leandroferreira.storyteller.normalization.merge.MergeNormalization
import br.com.leandroferreira.storyteller.normalization.merge.StepsMergerCoordinator
import br.com.leandroferreira.storyteller.normalization.merge.steps.StepToStepMerger
import br.com.leandroferreira.storyteller.utils.UnitsMapTransformation
import br.com.leandroferreira.storyteller.utils.UnitsNormalizationMap

class StepsMapNormalizationBuilder {

    companion object {
        fun reduceNormalizations(
            buildFunc: StepsMapNormalizationBuilder.() -> Unit
        ): UnitsNormalizationMap =
            StepsMapNormalizationBuilder().apply(buildFunc).build()
    }

    private var mergeNormalization: ((Map<Int, List<StoryUnit>>) -> Map<Int, StoryUnit>)? = null
    private val normalizations: MutableList<UnitsMapTransformation> = mutableListOf()

    /**
     * Adds a normalization.
     */
    fun addNormalization(
        normalization: UnitsMapTransformation
    ): StepsMapNormalizationBuilder = apply {
        normalizations.add(normalization)
    }

    fun addMergeNormalization(merge: (Map<Int, List<StoryUnit>>) -> Map<Int, StoryUnit>) {
        mergeNormalization = merge
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

        this.mergeNormalization  = mergeNormalization::mergeStepsMap
        normalizations.add(AddInBetween.spaces()::insert)
    }

    private fun build(): UnitsNormalizationMap = { units ->
        val merged = mergeNormalization!!.invoke(units)
        val reduced = reduceNormalizations(normalizations)

        reduced(merged)
    }

    private fun reduceNormalizations(
        normalizations: Iterable<UnitsMapTransformation>
    ): UnitsMapTransformation =
        normalizations.reduce { fn, gn -> { stories -> gn(fn(stories)) } }
}


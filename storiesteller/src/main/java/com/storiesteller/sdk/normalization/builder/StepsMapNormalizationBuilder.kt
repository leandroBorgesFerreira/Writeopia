package com.storiesteller.sdk.normalization.builder

import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.model.story.StoryTypes
import com.storiesteller.sdk.normalization.addinbetween.AddSteps
import com.storiesteller.sdk.normalization.merge.MergeLogic
import com.storiesteller.sdk.normalization.merge.MergeNormalization
import com.storiesteller.sdk.normalization.merge.StepsMergerCoordinator
import com.storiesteller.sdk.normalization.merge.steps.StepToStepMerger
import com.storiesteller.sdk.utils.alias.UnitsMapTransformation
import com.storiesteller.sdk.utils.alias.UnitsNormalizationMap

class StepsMapNormalizationBuilder {

    companion object {
        fun reduceNormalizations(
            buildFunc: StepsMapNormalizationBuilder.() -> Unit
        ): UnitsNormalizationMap =
            StepsMapNormalizationBuilder().apply(buildFunc).build()
    }

    private var mergeNormalization: ((Map<Int, List<StoryStep>>) -> Map<Int, StoryStep>)? = null
    private val normalizations: MutableList<UnitsMapTransformation> = mutableListOf()

    /**
     * Adds a normalization.
     */
    fun addNormalization(
        normalization: UnitsMapTransformation
    ): StepsMapNormalizationBuilder = apply {
        normalizations.add(normalization)
    }

    fun addMergeNormalization(merge: (Map<Int, List<StoryStep>>) -> Map<Int, StoryStep>) {
        mergeNormalization = merge
    }

    /**
     * Provides some useful normalizers. Use this to get a plug and play experience.
     */
    fun defaultNormalizers() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(
                StepsMergerCoordinator(
                    typeOfStep = StoryTypes.IMAGE.type,
                    typeOfGroup = StoryTypes.GROUP_IMAGE.type
                )
            )
            addMerger(
                StepsMergerCoordinator(
                    stepMerger = StepToStepMerger(),
                    typeOfStep = StoryTypes.MESSAGE.type,
                    typeOfGroup = null,
                    mergeLogic = MergeLogic::create
                )
            )
        }

        this.mergeNormalization = mergeNormalization::mergeSteps
        normalizations.add(AddSteps.spaces(skipFirst = true)::insert)
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


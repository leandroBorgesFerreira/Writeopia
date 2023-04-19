package br.com.leandroferreira.storyteller.normalization.merge

import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.merge.steps.StepToGroupMerger

typealias MergeLogicFunction = (StoryUnit, StoryUnit, String, String?) -> Boolean

class StepsMergerCoordinator(
    private val stepMerger: StepMerger = StepToGroupMerger(),
    private val typeOfStep: String,
    private val typeOfGroup: String? = null,
    private val mergeLogic: MergeLogicFunction = MergeLogic::lazy
) {

    fun canMerge(step1: StoryUnit, step2: StoryUnit): Boolean =
        mergeLogic(step1, step2, typeOfStep, typeOfGroup)

    fun merge(step1: StoryUnit, step2: StoryUnit): StoryUnit =
        stepMerger.merge(step1, step2, typeOfGroup ?: typeOfStep)

}

public object MergeLogic {
    fun eager(
        step1: StoryUnit,
        step2: StoryUnit,
        typeOfStep: String,
        typeOfGroup: String?
    ): Boolean =
        (step1.localPosition == step2.localPosition ||
            step1.localPosition + 1 == step2.localPosition) &&
            (step1.type == typeOfStep && step2.type == typeOfStep ||
                step1.type == typeOfGroup && step2.type == typeOfStep ||
                step1.type == typeOfStep && step2.type == typeOfGroup)

    fun lazy(
        step1: StoryUnit,
        step2: StoryUnit,
        typeOfStep: String,
        typeOfGroup: String?
    ): Boolean =
        step1.localPosition == step2.localPosition &&
            (step1.type == typeOfStep && step2.type == typeOfStep ||
                step1.type == typeOfGroup && step2.type == typeOfStep ||
                step1.type == typeOfStep && step2.type == typeOfGroup)


}

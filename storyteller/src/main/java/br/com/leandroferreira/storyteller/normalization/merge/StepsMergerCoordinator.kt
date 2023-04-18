package br.com.leandroferreira.storyteller.normalization.merge

import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.merge.steps.StepToGroupMerger

//Todo: Rename this class
class StepsMergerCoordinator(
    private val stepMerger: StepMerger = StepToGroupMerger(),
    private val typeOfStep: String,
    private val typeOfGroup: String? = null
) {

    fun canMerge(step1: StoryUnit, step2: StoryUnit): Boolean =
        step1.localPosition == step2.localPosition &&
            (step1.type == typeOfStep && step2.type == typeOfStep ||
            step1.type == typeOfGroup && step2.type == typeOfStep ||
            step1.type == typeOfStep && step2.type == typeOfGroup)

    fun merge(step1: StoryUnit, step2: StoryUnit): StoryUnit =
        stepMerger.merge(step1, step2, typeOfGroup ?: typeOfStep)

}

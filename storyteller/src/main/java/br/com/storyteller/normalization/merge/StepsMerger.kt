package br.com.storyteller.normalization.merge

import br.com.storyteller.model.StoryUnit

class StepsMerger(
    private val stepToGroupMerger: StepToGroupMerger = StepToGroupMerger(),
    private val typeOfStep: String,
    private val typeOfGroup: String
) {

    fun canMerge(step1: StoryUnit, step2: StoryUnit): Boolean =
        step1.localPosition == step2.localPosition &&
            (step1.type == typeOfStep && step2.type == typeOfStep ||
            step1.type == typeOfGroup && step2.type == typeOfStep ||
            step1.type == typeOfStep && step2.type == typeOfGroup)

    fun merge(step1: StoryUnit, step2: StoryUnit): StoryUnit =
        stepToGroupMerger.merge(step1, step2, typeOfGroup)

}

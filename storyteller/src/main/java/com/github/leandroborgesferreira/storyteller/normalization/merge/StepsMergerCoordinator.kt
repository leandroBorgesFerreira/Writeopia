package com.github.leandroborgesferreira.storyteller.normalization.merge

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit
import com.github.leandroborgesferreira.storyteller.normalization.merge.steps.StepToGroupMerger

typealias MergeLogicFunction = (StoryUnit, StoryUnit, String, String?) -> Boolean

class StepsMergerCoordinator(
    private val stepMerger: StepMerger = StepToGroupMerger(),
    private val typeOfStep: String,
    private val typeOfGroup: String? = null,
    private val mergeLogic: MergeLogicFunction = MergeLogic::create
) {

    fun canMerge(step1: StoryStep, step2: StoryStep): Boolean =
        mergeLogic(step1, step2, typeOfStep, typeOfGroup)

    fun merge(step1: StoryStep, step2: StoryStep): StoryStep =
        stepMerger.merge(step1, step2, typeOfGroup ?: typeOfStep)

}

public object MergeLogic {

    fun create(
        step1: StoryUnit,
        step2: StoryUnit,
        typeOfStep: String,
        typeOfGroup: String?
    ): Boolean =
        (step1.type == typeOfStep && step2.type == typeOfStep ||
            step1.type == typeOfGroup && step2.type == typeOfStep ||
            step1.type == typeOfStep && step2.type == typeOfGroup)

}

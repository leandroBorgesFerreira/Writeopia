package com.github.leandroferreira.storyteller.normalization.merge

import com.github.leandroferreira.storyteller.model.story.StoryUnit
import com.github.leandroferreira.storyteller.normalization.merge.steps.StepToGroupMerger

typealias MergeLogicFunction = (StoryUnit, StoryUnit, String, String?) -> Boolean

class StepsMergerCoordinator(
    private val stepMerger: StepMerger = StepToGroupMerger(),
    private val typeOfStep: String,
    private val typeOfGroup: String? = null,
    private val mergeLogic: MergeLogicFunction = MergeLogic::create
) {

    fun canMerge(step1: StoryUnit, step2: StoryUnit): Boolean =
        mergeLogic(step1, step2, typeOfStep, typeOfGroup)

    fun merge(step1: StoryUnit, step2: StoryUnit): StoryUnit =
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

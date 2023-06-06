package com.github.leandroborgesferreira.storyteller.normalization.merge.steps

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.normalization.merge.StepMerger

/**
 * This [StepMerger] merges a 2 [StoryStep] into a new [StoryStep] containing the information of
 * both units.
 *
 * This can be used to create a bigger message from 2 smaller ones. Note: This class doesn't support
 * groupsMerger and will throw an exception if requested to create a groups. Use [StepToGroupMerger]
 * for this purpose.
 */
open class StepToStepMerger : StepMerger {

    override fun merge(step1: StoryStep, step2: StoryStep, type: String): StoryStep =
        if (step1.type == "message" && step2.type == "message") {
            step1.copy(
                text = "${step1.text}\n\n${step2.text}",
            )
        } else {
            throw IllegalStateException(
                "At the moment StepToStepMerger can only be used to merge messages"
            )
        }
}

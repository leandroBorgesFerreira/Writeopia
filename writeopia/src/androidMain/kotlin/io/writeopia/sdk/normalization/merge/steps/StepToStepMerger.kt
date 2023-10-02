package io.writeopia.sdk.normalization.merge.steps

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.normalization.merge.StepMerger

/**
 * This [StepMerger] merges a 2 [StoryStep] into a new [StoryStep] containing the information of
 * both units.
 *
 * This can be used to create a bigger message from 2 smaller ones. Note: This class doesn't support
 * groupsMerger and will throw an exception if requested to create a groups. Use [StepToGroupMerger]
 * for this purpose.
 */
open class StepToStepMerger : StepMerger {

    override fun merge(step1: StoryStep, step2: StoryStep, type: StoryType): StoryStep {
        val messageNumber = StoryTypes.MESSAGE.type.number
        return if (step1.type.number == messageNumber && step2.type.number == messageNumber) {
            step1.copy(
                text = "${step1.text}\n\n${step2.text}",
            )
        } else {
            throw IllegalStateException(
                "At the moment StepToStepMerger can only be used to merge messages"
            )
        }
    }
}

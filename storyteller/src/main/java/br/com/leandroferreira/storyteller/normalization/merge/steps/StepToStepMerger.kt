package br.com.leandroferreira.storyteller.normalization.merge.steps

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.normalization.merge.StepMerger

/**
 * This [StepMerger] merges a 2 [StoryUnit] into a new [StoryUnit] containing the information of
 * both units.
 *
 * This can be used to create a bigger message from 2 smaller ones. Note: This class doesn't support
 * groupsMerger and will throw an exception if requested to create a groups. Use [StepToGroupMerger]
 * for this purpose.
 */
open class StepToStepMerger : StepMerger {

    override fun merge(step1: StoryUnit, step2: StoryUnit, type: String): StoryUnit =
        if (step1.type == "message" && step2.type == "message") {
            val story1 = step1 as StoryStep
            val story2 = step2 as StoryStep

            story1.copy(
                text = "${story1.text}\n\n${story2.text}",
                localPosition = step2.localPosition
            )
        } else {
            throw IllegalStateException(
                "At the moment StepToStepMerger can only be used to merge messages"
            )
        }

    override fun groupsMerger(step1: GroupStep, step2: GroupStep): StoryUnit {
        throw IllegalStateException(
            "Step to step merger should not be used to merge groups"
        )
    }
}

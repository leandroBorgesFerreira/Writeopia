package br.com.storyteller.normalization.merge.steps

import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit
import br.com.storyteller.normalization.merge.StepMerger

/**
 * Todo: I need to recheck the type system of this library to avoid unnecessary casts
 */
open class StepToStepMerger : StepMerger {

    override fun merge(step1: StoryUnit, step2: StoryUnit, type: String): StoryUnit =
        if (step1.type == "message" && step2.type == "message") {
            val story1 = step1 as StoryStep
            val story2 = step2 as StoryStep

            story1.copy(text = "${story1.text}\n${story2.text}")
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

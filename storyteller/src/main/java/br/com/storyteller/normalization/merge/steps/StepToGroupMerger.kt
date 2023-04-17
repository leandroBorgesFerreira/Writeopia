package br.com.storyteller.normalization.merge.steps

import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit
import br.com.storyteller.normalization.merge.StepMerger

/**
 * Todo: Write documentation
 */
open class StepToGroupMerger : StepMerger {

    override fun merge(step1: StoryUnit, step2: StoryUnit, type: String): StoryUnit =
        when {
            step1 is StoryStep && step2 is StoryStep ->
                GroupStep(
                    id = "id",
                    type = type,
                    localPosition = step1.localPosition,
                    steps = listOf(step1, step2)
                )

            step1 is GroupStep && step2 is StoryStep -> step1.copy(
                steps = listOf(step2) + step1.steps
            )

            step1 is StoryStep && step2 is GroupStep -> step2.copy(
                    localPosition = step1.localPosition,
                    steps = listOf(step1) + step2.steps
                )

            step1 is GroupStep && step2 is GroupStep -> groupsMerger(step1, step2)

            else -> throw IllegalArgumentException(
                "story units must be either a GroupStep or StoryStep"
            )
        }

    override fun groupsMerger(step1: GroupStep, step2: GroupStep): StoryUnit =
        step1.copy(steps = step1.steps + step2.steps)
}

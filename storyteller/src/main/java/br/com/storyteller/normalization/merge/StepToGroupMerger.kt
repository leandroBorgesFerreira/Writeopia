package br.com.storyteller.normalization.merge

import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit

/**
 * Todo: Write documentation
 */
open class StepToGroupMerger {

    fun merge(step1: StoryUnit, step2: StoryUnit, type: String): StoryUnit =
        when {
            step1 is StoryStep && step2 is StoryStep ->
                GroupStep(
                    id = "id",
                    type = type,
                    localPosition = step1.localPosition,
                    steps = listOf(step1, step2)
                )

            step1 is GroupStep && step2 is StoryStep -> step1.copy(steps = step1.steps + step2)

            step1 is StoryStep && step2 is GroupStep ->
                step2.copy(localPosition = step1.localPosition, steps = step2.steps + step1)

            step1 is GroupStep && step2 is GroupStep -> groupsMerger(step1, step2)


            else -> throw IllegalArgumentException(
                "story units must be either a GroupStep or StoryStep"
            )
        }

    //Todo: This can be done by composition
    protected open fun groupsMerger(step1: GroupStep, step2: GroupStep): StoryUnit =
        step1.copy(steps = step1.steps + step2.steps)
}

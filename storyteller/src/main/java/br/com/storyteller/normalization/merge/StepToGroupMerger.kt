package br.com.storyteller.normalization.merge

import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit

/**
 * Todo: Write documentation
 */
class StepToGroupMerger {

    fun merge(step1: StoryUnit, step2: StoryUnit, type: String): StoryUnit =
        when {
            step1 is StoryStep && step2 is StoryStep -> {
                GroupStep(
                    id = "id",
                    type = type,
                    localPosition = step1.localPosition,
                    steps = listOf(step1, step2)
                )
            }

            step1 is GroupStep && step2 is StoryStep -> {
                step1.copy(steps = step1.steps + step2)
            }

            step1 is StoryStep && step2 is GroupStep -> {
                step2.copy(localPosition = step1.localPosition, steps = step2.steps + step1)
            }

            step1 is GroupStep && step2 is GroupStep -> {
                step1.copy(steps = step1.steps + step2.steps)
            }

            else -> throw IllegalArgumentException(
                "story units must be either a GroupStep or StoryStep"
            )
        }
}

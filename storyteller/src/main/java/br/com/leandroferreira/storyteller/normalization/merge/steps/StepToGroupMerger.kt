package br.com.leandroferreira.storyteller.normalization.merge.steps

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.merge.StepMerger
import java.util.UUID

/**
 * Todo: Write documentation
 */
open class StepToGroupMerger : StepMerger {

    override fun merge(step1: StoryUnit, step2: StoryUnit, type: String): StoryUnit =
        when {
            step1 is StoryStep && step2 is StoryStep -> {
                val groupId = UUID.randomUUID().toString()

                GroupStep(
                    id = groupId,
                    type = type,
                    localPosition = step2.localPosition,
                    steps = listOf(step1.copy(parentId = groupId), step2.copy(parentId = groupId))
                )

            }

            step1 is GroupStep && step2 is StoryStep -> step1.copy(
                steps = listOf(step2) + step1.steps
            )

            step1 is StoryStep && step2 is GroupStep -> step2.copy(
                localPosition = step2.localPosition,
                steps = listOf(step1) + step2.steps
            )

            step1 is GroupStep && step2 is GroupStep -> groupsMerger(step1, step2)

            else -> throw IllegalArgumentException(
                "story units must be either a GroupStep or StoryStep"
            )
        }

    override fun groupsMerger(step1: GroupStep, step2: GroupStep): StoryUnit =
        step1.copy(
            steps = step1.steps + step2.steps,
            localPosition = step2.localPosition
        )
}

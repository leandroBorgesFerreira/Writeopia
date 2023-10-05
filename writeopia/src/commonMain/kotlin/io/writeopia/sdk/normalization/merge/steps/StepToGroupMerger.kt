package io.writeopia.sdk.normalization.merge.steps

import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.normalization.merge.StepMerger

/**
 * This [StepMerger] merges a 2 StoryStep in to a GroupStep or adds a StoryStep to an existing
 * GroupStep or merges two GroupStep.
 *
 * This merger can be used to create a image gallery from two individual images.
 */
open class StepToGroupMerger : StepMerger {

    override fun merge(step1: StoryStep, step2: StoryStep, type: StoryType): StoryStep =
        when {
            !step1.isGroup && !step2.isGroup -> {
                val groupId = GenerateId.generate()

                StoryStep(
                    id = groupId,
                    type = type,
                    steps = listOf(step1.copy(parentId = groupId), step2.copy(parentId = groupId))
                )
            }

            step1.isGroup && !step2.isGroup -> step1.copy(
                steps = listOf(step2.copy(parentId = step1.id)) + step1.steps
            )

            !step1.isGroup && step2.isGroup -> step2.copy(
                steps = listOf(step1.copy(parentId = step2.id)) + step2.steps
            )

            step1.isGroup && step2.isGroup -> step1.copy(
                steps = step1.steps + step2.steps.map { step ->
                    step.copy(parentId = step1.id)
                },
            )

            else -> throw IllegalArgumentException(
                "story units must be either a GroupStep or StoryStep"
            )
        }
}

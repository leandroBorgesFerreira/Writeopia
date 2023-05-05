package br.com.leandroferreira.storyteller.normalization.merge

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.normalization.merge.steps.StepToStepMerger
import br.com.leandroferreira.storyteller.utils.ListStoryData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MergeNormalizationTest {

    @Test
    fun `it should be possible to merge a group of images`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val mergedStep = mergeNormalization.mergeSteps(ListStoryData.imageStepsList())

        assertEquals(mergedStep.size, 1)
        assertTrue(mergedStep.first() is GroupStep)
        assertEquals(mergedStep.first().localPosition, 0)
    }

    @Test
    fun `it should be possible to merge a group of images with messages in the list`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val mergedStep = mergeNormalization.mergeSteps(ListStoryData.stepsList())

        assertEquals(3, mergedStep.size)
        assertTrue(mergedStep.first() is GroupStep)
        assertTrue(mergedStep[1] is StoryStep)
        assertTrue(mergedStep[2] is StoryStep)
        assertEquals("group_image", mergedStep.first().type)
    }

    @Test
    fun `it should be possible to merge a group of messages`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(
                StepsMergerCoordinator(
                    stepMerger = StepToStepMerger(),
                    typeOfStep = "message"
                )
            )
        }

        val mergedStep = mergeNormalization.mergeSteps(ListStoryData.messageStepsList())

        assertEquals(1, mergedStep.size)
        assertTrue(mergedStep.first() is StoryStep)
        assertEquals("message", mergedStep.first().type)
    }

    @Test
    fun `it should skip messages without mergers`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val last = StoryStep(
            id = "6",
            type = "unknown",
            localPosition = 6
        )

        val mergedStep = mergeNormalization.mergeSteps(ListStoryData.stepsList() + last)
        assertEquals(last, mergedStep.last())
    }

    @Test
    fun `the image always goes to the beginning of a group message`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val last = StoryStep(
            id = "6",
            type = "image",
            localPosition = 0
        )

        val story = listOf(
            GroupStep(
                id = "1",
                type = "group_image",
                localPosition = 0,
                steps = listOf(
                    StoryStep(
                        id = "2",
                        type = "image",
                        localPosition = 0,
                    )
                )
            ),
            last
        )

        val mergedStep = mergeNormalization.mergeSteps(story)

        assertEquals(last.id, (mergedStep.first() as GroupStep).steps.first().id)
    }

    @Test
    fun `elements should be merged together even they are not positioned consecutively`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val input = imageStepsListNonConsecutive()
        val initialSize = input.size
        val mergedSteps = mergeNormalization.mergeSteps(input)

        assertEquals("one image should be merged", initialSize - 1, mergedSteps.size)
        assertTrue("The first step should be now a group", mergedSteps.first() is GroupStep)
        assertEquals(2, (mergedSteps.first() as GroupStep).steps.size)
    }

    private fun imageStepsListNonConsecutive(): List<StoryStep> = buildList {
        add(
            StoryStep(
                id = "1",
                type = "image",
                localPosition = 0
            )
        )
        add(
            StoryStep(
                id = "2",
                type = "image",
                localPosition = 1
            )
        )
        add(
            StoryStep(
                id = "3",
                type = "image",
                localPosition = 0
            )
        )
    }

}

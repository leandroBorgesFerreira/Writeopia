package br.com.storyteller.normalization.merge

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.normalization.merge.MergeLogic
import br.com.leandroferreira.storyteller.normalization.merge.MergeNormalization
import br.com.leandroferreira.storyteller.normalization.merge.StepsMergerCoordinator
import br.com.leandroferreira.storyteller.normalization.merge.steps.StepToStepMerger
import br.com.storyteller.utils.StoryData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Ignore
import org.junit.Test

class MergeNormalizationTest {

    @Test
    fun `it should be possible to merge a group of images`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val mergedStep = mergeNormalization.mergeSteps(StoryData.imageStepsList())

        assertEquals(mergedStep.size, 1)
        assertTrue(mergedStep.first() is GroupStep)
        assertEquals(mergedStep.first().localPosition, 0)
    }

    @Test
    fun `it should be possible to merge a group of images with messages in the list`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val mergedStep = mergeNormalization.mergeSteps(StoryData.stepsList())

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

        val mergedStep = mergeNormalization.mergeSteps(StoryData.messageStepsList())

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

        val mergedStep = mergeNormalization.mergeSteps(StoryData.stepsList() + last)
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

        assertEquals(last, (mergedStep.first() as GroupStep).steps.first())
    }

    @Test
//    @Ignore
    fun `a list of consecutive messages should be merged when the merger is eager`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(
                StepsMergerCoordinator(
                    stepMerger = StepToStepMerger(),
                    typeOfStep = "message",
                    mergeLogic = MergeLogic::eager
                )
            )
        }

        val mergedMessages = mergeNormalization.mergeSteps(StoryData.messagesInLine())

        /*
         * This unit test is failing because only the first messages are getting merged in pairs...
         */
        assertEquals(1, mergedMessages.size)
    }
}

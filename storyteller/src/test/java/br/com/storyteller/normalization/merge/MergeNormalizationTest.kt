package br.com.storyteller.normalization.merge

import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryStep
import br.com.storyteller.utils.StoryData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MergeNormalizationTest {

    @Test
    fun `it should be possible to merge a group of images`() {
        val mergeNormalization = MergeNormalization.Builder()
            .addMerger(StepsMerger(typeOfStep = "image", typeOfGroup = "group_image"))
            .build()

        val mergedStep = mergeNormalization.mergeSteps(StoryData.imageStepsList())

        assertEquals(mergedStep.size, 1)
        assertTrue(mergedStep.first() is GroupStep)
        assertEquals(mergedStep.first().localPosition, 1)
    }

    @Test
    fun `it should be possible to merge a group of messages AND images`() {
        val mergeNormalization = MergeNormalization.Builder()
            .addMerger(StepsMerger(typeOfStep = "image", typeOfGroup = "group_image"))
            .addMerger(StepsMerger(typeOfStep = "message", typeOfGroup = "group_message"))
            .build()

        val mergedStep = mergeNormalization.mergeSteps(StoryData.stepsList())

        assertEquals(2, mergedStep.size)
        assertTrue(mergedStep.first() is GroupStep)
        assertTrue(mergedStep[1] is GroupStep)
        assertEquals("group_image", mergedStep.first().type)
        assertEquals("group_message", mergedStep[1].type)
    }

    @Test
    fun `it should skip messages without mergers`() {
        val mergeNormalization = MergeNormalization.Builder()
            .addMerger(StepsMerger(typeOfStep = "image", typeOfGroup = "group_image"))
            .addMerger(StepsMerger(typeOfStep = "message", typeOfGroup = "group_message"))
            .build()

        val last = StoryStep(
            id = "6",
            type = "unknown",
            localPosition = 6
        )

        val mergedStep = mergeNormalization.mergeSteps(StoryData.stepsList() + last)
        assertEquals(last, mergedStep.last())
    }
}

package br.com.storyteller.normalization

import br.com.storyteller.model.GroupStep
import br.com.storyteller.normalization.merge.MergeNormalization
import br.com.storyteller.normalization.merge.StepsMerger
import br.com.storyteller.normalization.position.PositionNormalization
import br.com.storyteller.utils.StoryData
import junit.framework.TestCase
import org.junit.Test

class StepsNormalizationTest {

    @Test
    fun `it should be possible to merge a group of messages AND images`() {
        val mergeNormalization = MergeNormalization.Builder()
            .addMerger(StepsMerger(typeOfStep = "image", typeOfGroup = "group_image"))
            .addMerger(StepsMerger(typeOfStep = "message", typeOfGroup = "group_message"))
            .build()

        val storiesNormalization = StepsNormalizationBuilder()
            .addNormalization(mergeNormalization::mergeSteps)
            .addNormalization(PositionNormalization::normalizePosition)
            .build()

        val mergedStep = storiesNormalization(StoryData.stepsList())

        TestCase.assertEquals(mergedStep.size, 2)
        TestCase.assertTrue(mergedStep.first() is GroupStep)
        TestCase.assertTrue(mergedStep[1] is GroupStep)
        TestCase.assertEquals("group_image", mergedStep.first().type)
        TestCase.assertEquals("group_message", mergedStep[1].type)
        TestCase.assertEquals(0, mergedStep.first().localPosition)
        TestCase.assertEquals(1, mergedStep[1].localPosition)
    }
}

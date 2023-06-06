package com.github.leandroborgesferreira.storyteller.normalization.merge

import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.normalization.merge.steps.StepToStepMerger
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import com.github.leandroborgesferreira.storyteller.utils.extensions.associateWithPosition
import com.github.leandroborgesferreira.storyteller.utils.extensions.toEditState
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

        val mergedStep = mergeNormalization.mergeSteps(MapStoryData.imageSimpleGroup())

        assertEquals(mergedStep.size, 1)
        assertTrue(mergedStep[0]?.isGroup ?: false)
    }

    @Test
    fun `it should be possible to merge a group of images with messages in the list`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val mergedStep = mergeNormalization.mergeSteps(MapStoryData.stepsList())

        assertEquals(3, mergedStep.size)
        assertTrue(mergedStep[0]?.isGroup ?: false)
        assertTrue(mergedStep[1] is StoryStep)
        assertTrue(mergedStep[2] is StoryStep)
        assertEquals("group_image", mergedStep[0]!!.type)
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

        val mergedStep =
            mergeNormalization.mergeSteps(MapStoryData.twoGroupsImageList())

        assertEquals("2 image groups should have been merged into one", 1, mergedStep.size)
        assertTrue("the first story unit should still be a GroupStep", mergedStep[0]?.isGroup ?: false)
        assertEquals(
            "the first story unit should still be a GroupImage",
            StoryType.GROUP_IMAGE.type,
            mergedStep[0]!!.type
        )
    }

    @Test
    fun `it should skip messages without mergers`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val last = listOf(
            StoryStep(
                localId = "6",
                type = "unknown",
            )
        )

        val steps = MapStoryData.stepsList()

        val mergedStep = mergeNormalization.mergeSteps(steps + Pair(steps.size, last))
        assertEquals(last.first(), mergedStep[mergedStep.size - 1])
    }

    @Test
    @Ignore("Evaluate if the image should actually go to beginning of the group")
    fun `the image always goes to the beginning of a group message`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(StepsMergerCoordinator(typeOfStep = "image", typeOfGroup = "group_image"))
        }

        val last = StoryStep(
            localId = "6",
            type = "image",
        )

        val story = listOf(
            StoryStep(
                localId = "1",
                type = "group_image",
                steps = listOf(
                    StoryStep(
                        localId = "2",
                        type = "image",
                    ),
                    last
                )
            ),
        ).associateWithPosition()

        val mergedStep = mergeNormalization.mergeSteps(story.toEditState())

        assertEquals(last.localId, (mergedStep[0] as StoryStep).steps.first().localId)
    }
}

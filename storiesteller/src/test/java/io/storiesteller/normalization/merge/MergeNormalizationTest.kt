package io.storiesteller.normalization.merge

import io.storiesteller.sdk.model.story.StoryTypes
import io.storiesteller.sdk.models.story.StoryStep
import io.storiesteller.sdk.models.story.StoryType
import io.storiesteller.sdk.normalization.merge.steps.StepToStepMerger
import io.storiesteller.utils.MapStoryData
import io.storiesteller.sdk.normalization.merge.MergeNormalization
import io.storiesteller.sdk.normalization.merge.StepsMergerCoordinator
import io.storiesteller.sdk.utils.extensions.associateWithPosition
import io.storiesteller.sdk.utils.extensions.toEditState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Ignore
import org.junit.Test

class MergeNormalizationTest {

    @Test
    fun `it should be possible to merge a group of images`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(
                StepsMergerCoordinator(
                    typeOfStep = StoryTypes.IMAGE.type,
                    typeOfGroup = StoryTypes.GROUP_IMAGE.type
                )
            )
        }

        val mergedStep = mergeNormalization.mergeSteps(MapStoryData.imageSimpleGroup())

        assertEquals(mergedStep.size, 1)
        assertTrue(mergedStep[0]?.isGroup ?: false)
    }

    @Test
    fun `it should be possible to merge a group of images with messages in the list`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(
                StepsMergerCoordinator(
                    typeOfStep = StoryTypes.IMAGE.type,
                    typeOfGroup = StoryTypes.GROUP_IMAGE.type
                )
            )
        }

        val mergedStep = mergeNormalization.mergeSteps(MapStoryData.stepsList())

        assertEquals(3, mergedStep.size)
        assertTrue(mergedStep[0]?.isGroup ?: false)
        assertTrue(mergedStep[1] is StoryStep)
        assertTrue(mergedStep[2] is StoryStep)
        assertEquals("group_image", mergedStep[0]!!.type.name)
    }

    @Test
    fun `it should be possible to merge a group of messages`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(
                StepsMergerCoordinator(
                    stepMerger = StepToStepMerger(),
                    typeOfStep = StoryTypes.MESSAGE.type
                )
            )
        }

        val mergedStep =
            mergeNormalization.mergeSteps(MapStoryData.twoGroupsImageList())

        assertEquals("2 image groups should have been merged into one", 1, mergedStep.size)
        assertTrue(
            "the first story unit should still be a GroupStep",
            mergedStep[0]?.isGroup ?: false
        )
        assertEquals(
            "the first story unit should still be a GroupImage",
            StoryTypes.GROUP_IMAGE.type,
            mergedStep[0]!!.type
        )
    }

    @Test
    fun `it should skip messages without mergers`() {
        val mergeNormalization = MergeNormalization.build {
            addMerger(
                StepsMergerCoordinator(
                    typeOfStep = StoryTypes.IMAGE.type,
                    typeOfGroup = StoryTypes.GROUP_IMAGE.type
                )
            )
        }

        val last = listOf(
            StoryStep(
                localId = "6",
                type = StoryType(
                    "unknown",
                    -1
                ),
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
            addMerger(
                StepsMergerCoordinator(
                    typeOfStep = StoryTypes.IMAGE.type,
                    typeOfGroup = StoryTypes.GROUP_IMAGE.type
                )
            )
        }

        val last = StoryStep(
            localId = "6",
            type = StoryTypes.IMAGE.type,
        )

        val story = listOf(
            StoryStep(
                localId = "1",
                type = StoryTypes.GROUP_IMAGE.type,
                steps = listOf(
                    StoryStep(
                        localId = "2",
                        type = StoryTypes.IMAGE.type,
                    ),
                    last
                )
            ),
        ).associateWithPosition()

        val mergedStep = mergeNormalization.mergeSteps(story.toEditState())

        assertEquals(last.localId, mergedStep[0]!!.steps.first().localId)
    }
}

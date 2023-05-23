package com.github.leandroferreira.storyteller.normalization.merge.steps

import com.github.leandroferreira.storyteller.model.story.GroupStep
import com.github.leandroferreira.storyteller.model.story.StoryType
import com.github.leandroferreira.storyteller.model.story.StoryStep
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class StepToGroupMergerTest {

    @Test
    fun `a simple merge should be possible`() {
        val merger = StepToGroupMerger()

        val image1 = StoryStep(
            localId = "1",
            type = "image",
        )

        val image2 = StoryStep(
            localId = "2",
            type = "image",
        )

        val result = merger.merge(image1, image2, "group_image")

        assertEquals("group_image", result.type)
        assertEquals(2, (result as GroupStep).steps.size)
        assertEquals(result.localId, result.steps[0].parentId)
        assertEquals(result.localId, result.steps[1].parentId)
    }

    @Test
    fun `when merging 2 groups all inner steps should have the same parent ID`() {
        val merger = StepToGroupMerger()

        val parent1Id = UUID.randomUUID().toString()
        val parent2Id = UUID.randomUUID().toString()

        val group1 =
            GroupStep(
                localId = parent1Id,
                type = StoryType.GROUP_IMAGE.type,
                steps = listOf(
                    StoryStep(
                        localId = "1",
                        type = "image",
                        parentId = parent1Id
                    ),
                    StoryStep(
                        localId = "2",
                        type = "image",
                        parentId = parent1Id
                    ),
                    StoryStep(
                        localId = "3",
                        type = "image",
                        parentId = parent1Id
                    )
                )
            )

        val group2 =
            GroupStep(
                localId = parent2Id,
                type = StoryType.GROUP_IMAGE.type,
                steps = listOf(
                    StoryStep(
                        localId = "11",
                        type = "image",
                        parentId = parent2Id
                    ),
                    StoryStep(
                        localId = "22",
                        type = "image",
                        parentId = parent2Id
                    ),
                    StoryStep(
                        localId = "33",
                        type = "image",
                        parentId = parent2Id
                    )
                )
            )


        val result = merger.merge(group1, group2, "group_image")

        assertEquals("group_image", result.type)
        assertEquals(6, (result as GroupStep).steps.size)

        result.steps.forEachIndexed { i, storyUnit ->
            assertEquals(
                "The step number $i should have the parent id: $parent1Id",
                parent1Id,
                storyUnit.parentId
            )
        }
    }
}

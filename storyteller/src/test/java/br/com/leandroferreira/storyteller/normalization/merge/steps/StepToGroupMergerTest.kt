package br.com.leandroferreira.storyteller.normalization.merge.steps

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StepType
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.utils.MapStoryData
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class StepToGroupMergerTest {

    @Test
    fun `a simple merge should be possible`() {
        val merger = StepToGroupMerger()

        val image1 = StoryStep(
            id = "1",
            type = "image",
        )

        val image2 = StoryStep(
            id = "2",
            type = "image",
        )

        val result = merger.merge(image1, image2, "group_image")

        assertEquals("group_image", result.type)
        assertEquals(2, (result as GroupStep).steps.size)
        assertEquals(result.id, result.steps[0].parentId)
        assertEquals(result.id, result.steps[1].parentId)
    }

    @Test
    fun `when merging 2 groups all inner steps should have the same parent ID`() {
        val merger = StepToGroupMerger()

        val parent1Id = UUID.randomUUID().toString()
        val parent2Id = UUID.randomUUID().toString()

        val group1 =
            GroupStep(
                id = parent1Id,
                type = StepType.GROUP_IMAGE.type,
                steps = listOf(
                    StoryStep(
                        id = "1",
                        type = "image",
                        parentId = parent1Id
                    ),
                    StoryStep(
                        id = "2",
                        type = "image",
                        parentId = parent1Id
                    ),
                    StoryStep(
                        id = "3",
                        type = "image",
                        parentId = parent1Id
                    )
                )
            )

        val group2 =
            GroupStep(
                id = parent2Id,
                type = StepType.GROUP_IMAGE.type,
                steps = listOf(
                    StoryStep(
                        id = "11",
                        type = "image",
                        parentId = parent2Id
                    ),
                    StoryStep(
                        id = "22",
                        type = "image",
                        parentId = parent2Id
                    ),
                    StoryStep(
                        id = "33",
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

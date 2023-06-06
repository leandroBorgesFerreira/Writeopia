package com.github.leandroborgesferreira.storyteller.normalization.builder

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StepsMapNormalizationBuilderTest {
    @Test
    fun `test of the default normalization provided by StepsNormalizationBuilder`() {
        val normalization = StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }

        val input = MapStoryData.complexList()
        val normalized = normalization(input)

        assertEquals(
            "The first non space StoryUnit should be a GroupStep",
            "group_image",
            normalized[1]!!.type
        )

        assertEquals(
            "There should be an space between all the story units",
            input.size * 2 + 2,
            normalized.size
        )
        assertEquals(
            "The images in the same position should be merged into GroupImage",
            "group_image",
            normalized[5]?.type
        )
        assertEquals(
            "The images in the same position should be merged into GroupImage",
            3,
            (normalized[5] as StoryStep).steps.size
        )
        assertEquals(
            "The last message should stay as it was",
            "message",
            normalized[normalized.size - 3]?.type
        )

        val group = (normalized[5])
        group!!.steps.forEach { storyUnit ->
            assertEquals(
                "The steps inside the group should reference it as the parent",
                group.localId,
                storyUnit.parentId
            )
        }
    }
}

package io.writeopia.normalization.builder

import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.utils.MapStoryData
import io.writeopia.sdk.normalization.builder.StepsMapNormalizationBuilder
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
            StoryTypes.GROUP_IMAGE.type,
            normalized[0]!!.type
        )

        assertEquals(
            "There should be an space between all the story units",
            input.size * 2 + 1,
            normalized.size
        )
        assertEquals(
            "The images in the same position should be merged into GroupImage",
            StoryTypes.GROUP_IMAGE.type,
            normalized[4]?.type
        )
        assertEquals(
            "The images in the same position should be merged into GroupImage",
            3,
            normalized[4]?.steps?.size
        )
        assertEquals(
            "The last message should stay as it was",
            StoryTypes.MESSAGE.type,
            normalized[normalized.size - 3]?.type
        )

        val group = (normalized[4])
        group!!.steps.forEach { storyUnit ->
            assertEquals(
                "The steps inside the group should reference it as the parent",
                group.id,
                storyUnit.parentId
            )
        }
    }
}

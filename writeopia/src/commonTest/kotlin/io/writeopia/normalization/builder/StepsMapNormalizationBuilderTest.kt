package io.writeopia.normalization.builder

import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.utils.MapStoryData
import io.writeopia.sdk.normalization.builder.StepsMapNormalizationBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class StepsMapNormalizationBuilderTest {
    @Test
    fun `test of the default normalization provided by StepsNormalizationBuilder`() {
        val normalization = StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }

        val input = MapStoryData.complexList()
        val normalized = normalization(input)

        assertEquals(
            StoryTypes.GROUP_IMAGE.type,
            normalized[0]!!.type,
            "The first non space StoryUnit should be a GroupStep"
        )

        assertEquals(
            input.size * 2 + 1,
            normalized.size,
            "There should be an space between all the story units"
        )
        assertEquals(
            StoryTypes.GROUP_IMAGE.type,
            normalized[4]?.type,
            "The images in the same position should be merged into GroupImage"
        )
        assertEquals(
            3,
            normalized[4]?.steps?.size,
            "The images in the same position should be merged into GroupImage"
        )
        assertEquals(
            StoryTypes.TEXT.type,
            normalized[normalized.size - 3]?.type,
            "The last message should stay as it was"
        )

        val group = (normalized[4])
        group!!.steps.forEach { storyUnit ->
            assertEquals(
                group.id,
                storyUnit.parentId,
                "The steps inside the group should reference it as the parent"
            )
        }
    }
}

package br.com.leandroferreira.storyteller.normalization

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.utils.StoryData
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StepsNormalizationBuilderTest {

    @Test
    fun `test of the default normalization provided by StepsNormalizationBuilder`() {
        val normalization = StepsNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }

        val input = StoryData.complexList()
        val normalized = normalization(input)

        assertEquals(
            "There should be an space between all the story units",
            15,
            normalized.size
        )
        assertEquals(
            "The images in the same position should be merged into GroupImage",
            "group_image",
            normalized[5].type
        )
        assertEquals(
            "The images in the same position should be merged into GroupImage",
            3,
            (normalized[5] as GroupStep).steps.size
        )
        assertEquals(
            "The last message should stay as it was",
            "message",
            normalized[normalized.lastIndex - 1].type
        )

        val group = (normalized[5] as GroupStep)
        group.steps.forEach { storyUnit ->
            assertEquals(
                "The steps inside the group should reference it as the parent",
                group.id,
                storyUnit.parentId
            )
        }

        normalized.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }
}

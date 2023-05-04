package br.com.leandroferreira.storyteller.normalization.builder

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.utils.MapStoryData
import junit.framework.TestCase
import org.junit.Test

class StepsMapNormalizationBuilderTest {
    @Test
    fun `test of the default normalization provided by StepsNormalizationBuilder`() {
        val normalization = StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }

        val input = MapStoryData.complexList()
        val normalized = normalization(input)

        TestCase.assertEquals(
            "There should be an space between all the story units",
            15,
            normalized.size
        )
        TestCase.assertEquals(
            "The images in the same position should be merged into GroupImage",
            "group_image",
            normalized[5]?.type
        )
        TestCase.assertEquals(
            "The images in the same position should be merged into GroupImage",
            3,
            (normalized[5] as GroupStep).steps.size
        )
        TestCase.assertEquals(
            "The last message should stay as it was",
            "message",
            normalized[normalized.size - 2]?.type
        )

        val group = (normalized[5] as GroupStep)
        group.steps.forEach { storyUnit ->
            TestCase.assertEquals(
                "The steps inside the group should reference it as the parent",
                group.id,
                storyUnit.parentId
            )
        }
    }
}

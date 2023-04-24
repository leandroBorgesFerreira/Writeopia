package br.com.leandroferreira.storyteller.normalization

import br.com.leandroferreira.storyteller.utils.StoryData
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StepsNormalizationBuilderTest {

    @Test
    fun `test of the default normalization provided by StepsNormalizationBuilder`() {
        val normalization = StepsNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }

        val normalized = normalization(StoryData.complexList())
        assertEquals(7, normalized.size)
        assertEquals("group_image", normalized[2].type)
        assertEquals("message", normalized.last().type)

        normalized.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }
}

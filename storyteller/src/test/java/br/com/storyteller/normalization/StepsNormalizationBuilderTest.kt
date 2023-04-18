package br.com.storyteller.normalization

import br.com.storyteller.utils.StoryData
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
    }
}

package br.com.leandroferreira.storyteller.normalization.addinbetween

import br.com.leandroferreira.storyteller.utils.StoryData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class AddInBetweenTest {

    @Test
    fun `it should be possible to add spacers between a list of stories`() {
        val input = StoryData.imageGroup()
        val addInBetween = AddInBetween.spaces()

        val result = addInBetween.insert(input)

        result.forEachIndexed { index, storyUnit -> assertEquals(index, storyUnit.localPosition) }
        result.forEachIndexed { index, storyUnit ->
            val even = index % 2 == 0

            if (even) {
                assertEquals("space", storyUnit.type)
            } else {
                assertNotEquals("space", storyUnit.type)
            }
        }
    }
}

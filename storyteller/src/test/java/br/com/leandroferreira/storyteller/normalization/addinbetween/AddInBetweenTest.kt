package br.com.leandroferreira.storyteller.normalization.addinbetween

import br.com.leandroferreira.storyteller.utils.ListStoryData
import br.com.leandroferreira.storyteller.utils.MapStoryData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.fail
import org.junit.Test

class AddInBetweenTest {

    @Test
    fun `it should be possible to add spacers between a LIST of stories`() {
        val input = ListStoryData.imageGroup()
        val initialSize = input.size
        val addInBetween = AddInBetween.spaces()

        val result = addInBetween.insert(input)

        assertEquals(initialSize * 2 + 1, result.size)
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

    @Test
    fun `it should be possible to add spacers between a MAP of stories`() {
        val input = MapStoryData.imageGroup()
        val initialSize = input.size
        val addInBetween = AddInBetween.spaces()

        val result = addInBetween.insert(input)

        assertEquals(initialSize * 2 + 1, result.size)
        result.values.forEachIndexed { index, storyUnit ->
            val even = index % 2 == 0

            if (even) {
                assertEquals("space", storyUnit.type)
            } else {
                assertNotEquals("space", storyUnit.type)
            }
        }
    }

    @Test
    fun `it should add only when necessary`() {
        val input = ListStoryData.spacedImageStepsList()
        val addInBetween = AddInBetween.spaces()

        val result = addInBetween.insert(input)

        result.reduce { acc, storyUnit ->
            if (acc.type == "space" && storyUnit.type == "space") {
                fail("The AddInBetween should not add repeated StoryUnits, " +
                    "it should only add the missing ones.")
            }

            storyUnit
        }
    }

    @Test
    fun `it should filter repeated spaces`() {
        val input = ListStoryData.spaces()
        val result = AddInBetween.spaces().insert(input)

        assertEquals("only one item should be in the result",1, result.size)
        assertEquals("only one space should be in the result", "space", result.first().type)
    }
}

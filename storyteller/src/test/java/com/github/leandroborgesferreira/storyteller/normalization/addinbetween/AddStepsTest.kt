package com.github.leandroborgesferreira.storyteller.normalization.addinbetween

import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.utils.ListStoryData
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.fail
import org.junit.Test

class AddStepsTest {

    @Test
    fun `it should be possible to add spacers between a LIST of stories`() {
        val input = ListStoryData.imageGroup()
        val initialSize = input.size
        val addSteps = AddSteps.spaces(skipFirst = false)

        val result = addSteps.insert(input)

        assertEquals(initialSize * 2 + 2, result.size)
        result.forEachIndexed { index, storyUnit ->
            val isEven = index % 2 == 0
            val isLast = index == result.lastIndex

            when {
                isLast -> assertEquals(StoryType.LARGE_SPACE.type, storyUnit.type)
                isEven -> assertEquals("space", storyUnit.type)
                !isEven -> assertNotEquals("space", storyUnit.type)
            }
        }
    }

    @Test
    fun `it should be possible to add spacers between a MAP of stories`() {
        val input = MapStoryData.imageGroup()
        val initialSize = input.size
        val addSteps = AddSteps.spaces(skipFirst = false)

        val result = addSteps.insert(input)

        assertEquals(initialSize * 2 + 2, result.size)
        val resultList = result.values.toList()
        resultList.forEachIndexed { index, storyUnit ->
            val isEven = index % 2 == 0
            val isLast = index == resultList.lastIndex

            when {
                isLast -> assertEquals(StoryType.LARGE_SPACE.type, storyUnit.type)
                isEven -> assertEquals("space", storyUnit.type)
                !isEven -> assertNotEquals("space", storyUnit.type)
            }
        }
    }

    @Test
    fun `it should add only when necessary`() {
        val input = ListStoryData.spacedImageStepsList()
        val addSteps = AddSteps.spaces(skipFirst = false)

        val result = addSteps.insert(input)

        result.reduce { acc, storyUnit ->
            if (acc.type == "space" && storyUnit.type == "space") {
                fail(
                    "The AddInBetween should not add repeated StoryUnits, " +
                            "it should only add the missing ones."
                )
            }

            storyUnit
        }
    }

    @Test
    fun `it should filter repeated spaces`() {
        val input = ListStoryData.spaces()
        val result = AddSteps.spaces(skipFirst = false).insert(input)

        assertEquals("only 2 itemm should be in the result", 2, result.size)
        assertEquals("only one space should be in the result", "space", result.first().type)
    }

    @Test
    fun `running the normalization twice should not change the data`() {
        val input = ListStoryData.spacedImageStepsList()
        val addSteps = AddSteps.spaces(skipFirst = false)

        val result = addSteps.insert(input)
        val result2 = addSteps.insert(result)

        assertEquals(result.size, result2.size)
    }
}

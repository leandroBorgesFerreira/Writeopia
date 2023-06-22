package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import com.github.leandroborgesferreira.storyteller.utils.StoryStepFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class ContentHandlerTest {

    @Test
    fun `should be possible to add content correctly`() {
        val input = MapStoryData.imageStepsList()

        val contentHandler =
            ContentHandler(focusableTypes = setOf(StoryType.MESSAGE.type)) { _ -> mapOf() }

        val storyStep = StoryStep(type = StoryType.MESSAGE.type)
        val newStory = contentHandler.addNewContent(input, storyStep, 1)

        val expected = mapOf(
            0 to StoryStep(
                localId = "1",
                type = "image",
            ),
            1 to storyStep,
            2 to StoryStepFactory.space(),
            3 to StoryStep(
                localId = "2",
                type = "image",
            ),
            4 to StoryStep(
                localId = "3",
                type = "image",
            ),
        ).mapValues { (_, storyStep) ->
            storyStep.type
        }

        assertEquals(expected, newStory.mapValues {(_, storyStep) -> storyStep.type })
    }
}
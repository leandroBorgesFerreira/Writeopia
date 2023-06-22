package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.normalization.addinbetween.AddSteps
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import com.github.leandroborgesferreira.storyteller.utils.StoryStepFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class ContentHandlerTest {

    @Test
    fun `should be possible to add content correctly`() {
        val input = AddSteps.spaces(skipFirst = true).insert(MapStoryData.imageStepsList())

        val contentHandler =
            ContentHandler(focusableTypes = setOf(StoryType.MESSAGE.type)) { _ -> mapOf() }

        val storyStep = StoryStep(type = StoryType.MESSAGE.type)
        val newStory = contentHandler.addNewContent(input, storyStep, 2)

        val expected = mapOf(
            0 to StoryStep(type = StoryType.IMAGE.type),
            1 to StoryStepFactory.space(),
            2 to storyStep,
            3 to StoryStepFactory.space(),
            4 to StoryStep(type = StoryType.IMAGE.type),
            5 to StoryStepFactory.space(),
            6 to StoryStep(type = StoryType.IMAGE.type),
            7 to StoryStepFactory.space(),
            8 to StoryStep(type = StoryType.LARGE_SPACE.type),
        ).mapValues { (_, storyStep) ->
            storyStep.type
        }

        assertEquals(expected, newStory.mapValues {(_, storyStep) -> storyStep.type })
    }
}
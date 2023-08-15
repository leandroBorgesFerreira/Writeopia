package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
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
            ContentHandler(focusableTypes = setOf(StoryTypes.MESSAGE.type.number)) { _ -> mapOf() }

        val storyStep = StoryStep(type = StoryTypes.MESSAGE.type)
        val newStory = contentHandler.addNewContent(input, storyStep, 2)

        val expected = mapOf(
            0 to StoryStep(type = StoryTypes.IMAGE.type),
            1 to StoryStepFactory.space(),
            2 to storyStep,
            3 to StoryStepFactory.space(),
            4 to StoryStep(type = StoryTypes.IMAGE.type),
            5 to StoryStepFactory.space(),
            6 to StoryStep(type = StoryTypes.IMAGE.type),
            7 to StoryStepFactory.space(),
            8 to StoryStep(type = StoryTypes.LARGE_SPACE.type),
        ).mapValues { (_, storyStep) ->
            storyStep.type
        }

        assertEquals(expected, newStory.mapValues { (_, storyStep) -> storyStep.type })
    }

    @Test
    fun `when a line break happens, the text should be divided correctly`() {
        val contentHandler = ContentHandler { _ -> mapOf() }
        val storyStep = StoryStep(type = StoryTypes.MESSAGE.type, text = "line1\nline2")

        val (_, newState) = contentHandler.onLineBreak(
            mapOf(0 to storyStep),
            Action.LineBreak(storyStep, 0)
        )!!

        assertEquals("line1", newState.stories[0]!!.text)
    }
}
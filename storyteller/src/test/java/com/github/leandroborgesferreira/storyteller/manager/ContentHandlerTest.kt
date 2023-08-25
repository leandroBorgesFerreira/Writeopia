package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.command.Command
import com.github.leandroborgesferreira.storyteller.model.command.CommandFactory
import com.github.leandroborgesferreira.storyteller.model.command.CommandInfo
import com.github.leandroborgesferreira.storyteller.model.command.CommandTrigger
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.normalization.addinbetween.AddSteps
import com.github.leandroborgesferreira.storyteller.normalization.builder.StepsMapNormalizationBuilder
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import com.github.leandroborgesferreira.storyteller.utils.StoryStepFactory
import com.github.leandroborgesferreira.storyteller.utils.alias.UnitsNormalizationMap
import org.junit.Assert.assertEquals
import org.junit.Test

class ContentHandlerTest {

    @Test
    fun `should be possible to add content correctly`() {
        val input = AddSteps.spaces(skipFirst = true).insert(MapStoryData.imageStepsList())

        val contentHandler =
            ContentHandler(
                focusableTypes = setOf(StoryTypes.MESSAGE.type.number),
                stepsNormalizer = normalizer()
            )

        val storyStep =
            StoryStep(type = StoryTypes.MESSAGE.type)
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
        val contentHandler = ContentHandler(stepsNormalizer = normalizer())
        val storyStep = StoryStep(
            type = StoryTypes.MESSAGE.type,
            text = "line1\nline2"
        )

        val (_, newState) = contentHandler.onLineBreak(
            mapOf(0 to storyStep),
            Action.LineBreak(storyStep, 0)
        )!!

        assertEquals("line1", newState.stories[0]!!.text)
    }

    @Test
    fun `when check item command is WRITTEN, the command should be removed for the story text`() {
        val input = AddSteps.spaces(skipFirst = true).insert(MapStoryData.messagesInLine())
        val contentHandler = ContentHandler(stepsNormalizer = normalizer())
        val text = "Lalala"

        val storyStep = StoryStep(
            type = StoryTypes.MESSAGE.type,
            text = "-[]$text"
        )

        val position = 1
        val mutable = input.toMutableMap()
        mutable[position] = storyStep

        val newState = contentHandler.changeStoryType(
            currentStory = mutable,
            type = StoryTypes.CHECK_ITEM.type,
            position = position,
            CommandInfo(CommandFactory.checkItem(), CommandTrigger.WRITTEN)
        )

        val checkItemStory = newState.stories[position]

        assertEquals(StoryTypes.CHECK_ITEM.type, checkItemStory?.type)
        assertEquals(text, checkItemStory?.text)
    }

    @Test
    fun `when deleting stories, the focus should move correctly`() {
        val input = AddSteps.spaces(skipFirst = true).insert(MapStoryData.messagesInLine())
        val contentHandler = ContentHandler(stepsNormalizer = normalizer())
        val text = "Lalala"

        val storyStep = StoryStep(
            type = StoryTypes.MESSAGE.type,
            text = "# $text"
        )

        val position = 1
        val mutable = input.toMutableMap()
        mutable[position] = storyStep

        val newState = contentHandler.changeStoryType(
            currentStory = mutable,
            type = StoryTypes.H1.type,
            position = position,
            CommandInfo(CommandFactory.h1(), CommandTrigger.WRITTEN)
        )

        val h1Story = newState.stories[position]

        assertEquals(StoryTypes.H1.type, h1Story?.type)
        assertEquals(text, h1Story?.text)

        val deletePosition = 2

        val newState2 = contentHandler.deleteStory(
            Action.DeleteStory(newState.stories[deletePosition]!!, deletePosition),
            newState.stories
        )

        assertEquals(h1Story!!.id, newState2?.focusId,)
    }
}

private fun normalizer(): UnitsNormalizationMap =
    StepsMapNormalizationBuilder.reduceNormalizations {
        defaultNormalizers()
    }
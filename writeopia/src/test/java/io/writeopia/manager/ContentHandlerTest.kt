package io.writeopia.manager

import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.command.CommandFactory
import io.writeopia.sdk.model.command.CommandInfo
import io.writeopia.sdk.model.command.CommandTrigger
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.normalization.addinbetween.AddSteps
import io.writeopia.sdk.normalization.builder.StepsMapNormalizationBuilder
import io.writeopia.utils.MapStoryData
import io.writeopia.sdk.utils.StoryStepFactory
import io.writeopia.sdk.utils.alias.UnitsNormalizationMap
import io.writeopia.sdk.manager.ContentHandler
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
            text = "-[] $text"
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
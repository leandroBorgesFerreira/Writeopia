package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.manager.ContentHandler
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction
import com.github.leandroborgesferreira.storyteller.model.story.LastEdit
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import org.junit.Assert.*
import org.junit.Test

class PerStateBackstackManagerTest {

    private val backstackManager =
        PerStateBackstackManager(
            contentHandler = ContentHandler(
                stepsNormalizer = { storyMap ->
                    storyMap.mapValues { (_, storyList) -> storyList[0] }
                }
            )
        )

    @Test
    fun `when adding the first action, the manager should notify it is possible to revert`() {
        backstackManager.addAction(
            BackstackAction.StoryStateChange(
                StoryStep(type = StoryType.MESSAGE.type),
                position = 0
            )
        )

        assertEquals(true, backstackManager.canUndo.value)
        assertEquals(false, backstackManager.canRedo.value)
    }

    @Test
    fun `when adding and popping the stack, the manager should notify correctly`() {
        val mockState = StoryState(emptyMap(), LastEdit.Nothing)

        repeat(3) { i ->
            backstackManager.addAction(
                BackstackAction.StoryStateChange(
                    StoryStep(type = StoryType.MESSAGE.type),
                    position = i
                )
            )
        }

        assertEquals(true, backstackManager.canUndo.value)
        assertEquals(false, backstackManager.canRedo.value)

        backstackManager.previousState(mockState)

        assertEquals(true, backstackManager.canUndo.value)
        assertEquals(true, backstackManager.canRedo.value)

        backstackManager.previousState(mockState)

        assertEquals(true, backstackManager.canUndo.value)
        assertEquals(true, backstackManager.canRedo.value)

        backstackManager.previousState(mockState)

        assertEquals(false, backstackManager.canUndo.value)
        assertEquals(true, backstackManager.canRedo.value)
    }

    @Test
    fun `when adding a new story, it should no longer be possible to mover forward`() {
        val mockState = StoryState(emptyMap(), LastEdit.Nothing)

        repeat(3) { i ->
            backstackManager.addAction(
                BackstackAction.StoryStateChange(
                    StoryStep(type = StoryType.MESSAGE.type),
                    position = i
                )
            )
        }

        assertEquals(true, backstackManager.canUndo.value)
        assertEquals(false, backstackManager.canRedo.value)

        backstackManager.previousState(mockState)

        assertEquals(true, backstackManager.canUndo.value)
        assertEquals(true, backstackManager.canRedo.value)

        backstackManager.addAction(
            BackstackAction.StoryStateChange(
                StoryStep(type = StoryType.MESSAGE.type),
                position = 3
            )
        )

        assertEquals(true, backstackManager.canUndo.value)
        assertEquals(false, backstackManager.canRedo.value)
    }

    @Test
    fun `when adding many text some of the state should be merged`() {
        val mockState = StoryState(emptyMap(), LastEdit.Nothing)
        val stringBuilder = StringBuilder()

        repeat(20) { i ->
            stringBuilder.append(i)

            backstackManager.addAction(
                BackstackAction.StoryTextChange(
                    StoryStep(type = StoryType.MESSAGE.type, text = stringBuilder.toString()),
                    position = 0
                )
            )
        }

        backstackManager.previousState(mockState)
        backstackManager.previousState(mockState)

        assertEquals(false, backstackManager.canUndo.value)
    }
}
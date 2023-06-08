package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.change.DeleteInfo
import com.github.leandroborgesferreira.storyteller.model.change.LineBreakInfo
import com.github.leandroborgesferreira.storyteller.model.change.MergeInfo
import com.github.leandroborgesferreira.storyteller.model.change.MoveInfo
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.repository.StoriesRepository
import com.github.leandroborgesferreira.storyteller.utils.MainDispatcherRule
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import java.util.Stack

@OptIn(ExperimentalCoroutinesApi::class)
class StoryTellerManagerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val imagesInLineRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.imageStepsList()
    }

    private val imageGroupRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.imageGroup()
    }

    private val messagesRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.messagesInLine()
    }

    private val singleMessageRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.singleMessage()
    }

    private val complexMessagesRepository: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.syncHistory()
    }


    @Test
    fun `one space has to be added between steps`() = runTest {
        val storyManager = StoryTellerManager()
        val oldSize = messagesRepo.history().size

        storyManager.initStories(messagesRepo.history())

        val newStory = storyManager.currentStory.value.stories

        assertEquals("a space should be added between each step", oldSize * 2 + 2, newStory.size)
    }

    @Test
    fun `when a line break happens, one (and only one) new item should be created`() {
        val input = MapStoryData.singleCheckItem()
        val checkItem = input[0]

        val storyManager = StoryTellerManager().apply {
            initStories(input)
        }

        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(
            LineBreakInfo(storyStep = checkItem!!, position = 1)
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals("the first item should be a check_item", "check_item", newStory[1]!!.type)
        assertEquals("the second item should be a check_item", "check_item", newStory[3]!!.type)
        assertEquals("the size of the story should be 5", currentStory.size + 2, newStory.size)
    }

    @Test
    fun `merge request should work`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imagesInLineRepo.history())

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 3
        val positionTo = 1
        val sender = currentStory[positionFrom]!!
        val receiver = currentStory[positionTo]!!

        assertTrue("The first step is not a GroupStep", currentStory[1] is StoryStep)

        storyManager.mergeRequest(
            MergeInfo(
                sender = sender,
                receiver = receiver,
                positionFrom = positionFrom,
                positionTo = positionTo,
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "One image should be removed and one space",
            initialSize - 2,
            newStory.size
        )
        assertEquals("The first step should be now a GroupStep", true, newStory[1]?.isGroup)
    }

    @Test
    fun `merge request should work2`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imagesInLineRepo.history())

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 3
        val positionTo = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(initialSize - 2, newStory.size)
        assertEquals("The first image should be a GroupImage now", true, newStory[1]?.isGroup)
        assertTrue("Other images should still exist", newStory[3] is StoryStep)
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imagesInLineRepo.history())

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 3
        val positionTo = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newHistory = storyManager.currentStory.value.stories

        assertEquals("One space and one image were removed", initialSize - 2, newHistory.size)
        assertEquals(
            "The first message should be a GroupImage instead of a Image now",
            true,
            newHistory[1]?.isGroup
        )
        assertEquals(
            "The new created GroupImage should have 2 images",
            2,
            newHistory[1]!!.steps.size
        )

        repeat(2) {
            val newHistory2 = storyManager.currentStory.value.stories

            val newPositionFrom = newHistory2.size - 3
            val newPositionTo = 1

            storyManager.mergeRequest(
                MergeInfo(
                    receiver = newHistory2[newPositionTo]!!,
                    sender = newHistory2[newPositionFrom]!!,
                    positionTo = newPositionTo,
                    positionFrom = newPositionFrom
                )
            )
        }

        val newHistory3 = storyManager.currentStory.value.stories

        assertEquals(
            "The minimum side should be 4 (space, group, space, large_space)",
            4,
            newHistory3.size
        )
        assertEquals("The GroupImage should still exist", true, newHistory3[1]?.isGroup)
        assertEquals(
            "Now the group has 3 images",
            3,
            newHistory3[1]!!.steps.size
        )
    }

    @Test
    fun `it should be possible to merge an image inside a message group`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imageGroupRepo.history())

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size
        val initialImageGroupSize = currentStory[1]!!.steps.size

        val positionFrom = currentStory.size - 3
        val positionTo = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals("One image and one space were removed", initialSize - 2, newStory.size)
        assertTrue(newStory[1]?.isGroup == true)
        assertEquals(
            "One element was added to the GroupStep",
            initialImageGroupSize + 1,
            newStory[1]!!.steps.size
        )
    }

    @Test
    fun `it should be possible to merge an image outside a message group`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imageGroupRepo.history())

        val currentStory = storyManager.currentStory.value.stories
        val initialGroupSize = currentStory[1]!!.steps.size

        val positionTo = currentStory.size - 3
        val positionFrom = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!.steps[0],
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "The image should be now in the position 3, because of spaces.",
            "group_image",
            newStory[3]!!.type
        )
        assertEquals(
            "The new story now it the GroupImage",
            initialGroupSize - 1,
            newStory[1]!!.steps.size
        )
    }

    @Test
    @Ignore
    fun `when moving outside of a group, the parent Id should be null now`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imageGroupRepo.history())

        val currentStory = storyManager.currentStory.value.stories

        val positionTo = currentStory.size - 2
        val positionFrom = 1

        val storyToMove = currentStory[positionFrom]!!.steps[0]

        storyManager.moveRequest(
            MoveInfo(
                storyUnit = storyToMove,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.currentStory.value.stories
        val lastContentStep = newStory[newStory.size - 3]!!

        assertEquals(
            "The last StoryUnit should be an image.",
            "image",
            lastContentStep.type
        )
        assertEquals(
            "The image should be in the correct place now.",
            storyToMove.id,
            lastContentStep.id
        )
        assertNull(
            "The parent of the separated image, should not be there.",
            lastContentStep.parentId
        )
        assertFalse(
            "The moved image should not be in the group anymore",
            newStory[positionFrom]!!.steps.any { storyUnit ->
                storyUnit.id == storyToMove.id
            })
    }

    @Test
    fun `it should be possible to switch images places`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imagesInLineRepo.history())

        val currentStory = storyManager.currentStory.value.stories
        val storyUnitToMove = currentStory[1]!!

        storyManager.moveRequest(
            MoveInfo(currentStory[1]!!, 1, 4)
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "The first story should have been moved",
            newStory[3]!!.id,
            storyUnitToMove.id
        )
    }

    @Test
    fun `deleting and leave a single element in a group destroys the group`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(imageGroupRepo.history())

        val currentStory = storyManager.currentStory.value.stories
        assertEquals(
            "initial the story unit should be a group",
            "group_image",
            currentStory[1]!!.type
        )

        val lastImageInsideGroup = { storyManager.currentStory.value.stories[1]!!.steps.last() }

        storyManager.onDelete(
            DeleteInfo(
                storyUnit = lastImageInsideGroup(),
                position = 1
            )
        )

        storyManager.onDelete(
            DeleteInfo(
                storyUnit = lastImageInsideGroup(),
                position = 1
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "the group become just an image because there's only a single image",
            "image",
            newStory[1]!!.type
        )
    }

    @Test
    fun `when deleting a message it should not leave consecutive spaces`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(messagesRepo.history())

        storyManager.onDelete(
            DeleteInfo(
                storyManager.currentStory.value.stories[3]!!, 3
            )
        )

        val stack: Stack<StoryStep> = Stack()

        storyManager.currentStory.value.stories.forEach { (_, storyUnit) ->
            if (stack.isNotEmpty() && stack.peek().type == "space" && storyUnit.type == "space") {
                fail("Consecutive spaces happened.")
            }

            stack.add(storyUnit)
        }
    }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - simple`() =
        runTest {
            val storyManager = StoryTellerManager()
            storyManager.initStories(singleMessageRepo.history())

            val stories = storyManager.currentStory.value.stories
            val initialSize = stories.size

            storyManager.onLineBreak(LineBreakInfo(stories[1]!!, position = 1))

            assertEquals(
                "2 new stories should have been added",
                initialSize + 2,
                storyManager.currentStory.value.stories.size
            )
        }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - complex`() =
        runTest {
            val storyManager = StoryTellerManager()
            storyManager.initStories(messagesRepo.history())

            val stories = storyManager.currentStory.value.stories
            val initialSize = stories.size

            storyManager.onLineBreak(LineBreakInfo(stories[1]!!, 1))

            assertEquals(
                "2 new stories should have been added",
                initialSize + 2,
                storyManager.currentStory.value.stories.size
            )
        }

    @Test
    @Ignore
    fun `Complex move case1`() = runTest {
        /**
         * Steps:
         * 1 - Make 3 single images into a group
         * - Check that the 3 images are in a group
         * 2 - Move one image away.
         * - Check that the correct image was moved correctly
         */
        val storyManager = StoryTellerManager()
        storyManager.initStories(complexMessagesRepository.history())

        val stories = storyManager.currentStory.value.stories

        val positionTo = 1
        val positionFrom = 3
        storyManager.mergeRequest(
            MergeInfo(
                receiver = stories[positionTo]!!,
                sender = stories[positionFrom]!!,
                positionFrom = positionFrom,
                positionTo = positionTo,
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals("The images should have been merged", 2, (newStory[1]!!).steps.size)

        val stories2 = storyManager.currentStory.value.stories

        val positionTo2 = 1
        val positionFrom2 = 3
        storyManager.mergeRequest(
            MergeInfo(
                receiver = (stories2[positionTo2]!!).steps.first(),
                sender = stories2[positionFrom2]!!,
                positionFrom = positionFrom2,
                positionTo = positionTo2,
            )
        )

        val newStory2 = storyManager.currentStory.value.stories

        assertEquals(
            "The images should have been merged",
            3,
            (newStory2[1]!!).steps.distinctBy { storyUnit -> storyUnit.localId }.size
        )

        val positionFrom3 = 1
        val positionTo3 = 4
        val storyToMove = (newStory[positionFrom3]!!).steps.first()
        storyManager.moveRequest(
            MoveInfo(
                storyUnit = storyToMove,
                positionFrom = positionFrom3,
                positionTo = positionTo3,
            )
        )

        val newStory3 = storyManager.currentStory.value.stories

        assertEquals(
            "One image should have been separated",
            2,
            (newStory3[1]!!).steps.size
        )
        assertEquals(
            "The correct StoryUnit should have been moved",
            storyToMove.id,
            newStory3[5]!!.id
        )
    }

    @Test
    fun `it should be possible to add content and undo it - 1 unit`() {
        val storyManager = StoryTellerManager()
        val input = MapStoryData.singleCheckItem()

        storyManager.initStories(input)
        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(LineBreakInfo(input[0]!!, 1))
        storyManager.undo()

        assertEquals(currentStory.size, storyManager.currentStory.value.stories.size)
    }

    @Test
    fun `it should be possible to add content and undo it - many units`() {
        val storyManager = StoryTellerManager()
        val input = MapStoryData.singleCheckItem()

        storyManager.initStories(input)
        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(LineBreakInfo(input[0]!!, 1))
        storyManager.onLineBreak(LineBreakInfo(input[0]!!, 3))
        storyManager.onLineBreak(LineBreakInfo(input[0]!!, 5))
        storyManager.undo()
        storyManager.undo()
        storyManager.undo()

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "The size of the story can't have changed",
            currentStory.size,
            newStory.size
        )

        currentStory.values.zip(newStory.values).forEach { (storyUnit1, storyUnit2) ->
            if (storyUnit1.type != storyUnit2.type) fail()

            if (storyUnit1.type != StoryType.SPACE.type &&
                storyUnit1.type != StoryType.LARGE_SPACE.type
            ) {
                assertEquals(storyUnit1.id, storyUnit2.id)
            }
        }
    }

    @Test
    fun `spaces should be correct in the story`() {
        val storyManager = StoryTellerManager()
        val input = MapStoryData.singleCheckItem()

        storyManager.run {
            initStories(input)

            onLineBreak(LineBreakInfo(input[0]!!, 1))
            onLineBreak(LineBreakInfo(input[0]!!, 3))
            onLineBreak(LineBreakInfo(input[0]!!, 5))

            undo()
            undo()
            undo()

            redo()
            redo()
        }

        val newStory = storyManager.currentStory.value.stories

        newStory.forEach { (position, storyUnit) ->
            val isEven = position % 2 == 0

            if (isEven) {
                assertEquals("space", storyUnit.type)
            } else {
                assertNotEquals("space", storyUnit.type)
            }
        }
    }

    @Test
    fun `initializing the stories 2 times should not add the last story unit twice`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.initStories(complexMessagesRepository.history())

        val stories = storyManager.currentStory.value.stories
        assertEquals(StoryType.LARGE_SPACE.type, stories.values.last().type)

        storyManager.initStories(stories)

        val newStories = storyManager.currentStory.value.stories
        val storyList = newStories.values.toList()

        assertEquals(StoryType.LARGE_SPACE.type, storyList.last().type)
        assertNotEquals(StoryType.LARGE_SPACE.type, storyList[storyList.lastIndex - 1].type)
    }
}
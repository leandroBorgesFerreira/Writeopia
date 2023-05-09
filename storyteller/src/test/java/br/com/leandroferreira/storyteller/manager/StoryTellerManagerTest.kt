package br.com.leandroferreira.storyteller.manager

import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.utils.MainDispatcherRule
import br.com.leandroferreira.storyteller.utils.MapStoryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.Stack

@OptIn(ExperimentalCoroutinesApi::class)
class StoryTellerManagerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val imagesInLineRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryUnit> = MapStoryData.imageStepsList()
    }

    private val imageGroupRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryUnit> = MapStoryData.imageGroup()
    }

    private val messagesRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryUnit> = MapStoryData.messagesInLine()
    }

    private val singleMessageRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryUnit> = MapStoryData.singleMessage()
    }

    private val complexMessagesRepository: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryUnit> = MapStoryData.syncHistory()
    }


    @Test
    fun `one space has to be added between steps`() = runTest {
        val storyManager = StoryTellerManager()
        val oldSize = messagesRepo.history().size

        storyManager.addStories(messagesRepo.history())

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals("a space should be added between each step", oldSize * 2 + 1, newStory.size)
    }

    @Test
    fun `merge request should work`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imagesInLineRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 2
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

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "One image should be removed and one space",
            initialSize - 2,
            newStory.size
        )
        assertTrue("The first step should be now a GroupStep", newStory[1] is GroupStep)
    }

    @Test
    fun `merge request should work2`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imagesInLineRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 2
        val positionTo = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals(initialSize - 2, newStory.size)
        assertTrue("The first image should be a GroupImage now", newStory[1] is GroupStep)
        assertTrue("Other images should still exist", newStory[3] is StoryStep)
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imagesInLineRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 2
        val positionTo = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newHistory = storyManager.normalizedStepsState.value.stories

        assertEquals("One space and one image were removed", initialSize - 2, newHistory.size)
        assertTrue(
            "The first message should be a GroupImage instead of a Image now",
            newHistory[1] is GroupStep
        )
        assertEquals(
            "The new created GroupImage should have 2 images",
            2,
            (newHistory[1] as GroupStep).steps.size
        )

        repeat(2) {
            val newHistory2 = storyManager.normalizedStepsState.value.stories

            val newPositionFrom = newHistory2.size - 2
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

        val newHistory3 = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "The minimum side should be 3 (space, group, space)",
            3,
            newHistory3.size
        )
        assertTrue("The GroupImage should still exist", newHistory3[1] is GroupStep)
        assertEquals(
            "Now the group has 3 images",
            3,
            (newHistory3[1] as GroupStep).steps.size
        )
    }

    @Test
    fun `it should be possible to merge an image inside a message group`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imageGroupRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories
        val initialSize = currentStory.size
        val initialImageGroupSize = (currentStory[1] as GroupStep).steps.size

        val positionFrom = currentStory.size - 2
        val positionTo = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals("One image and one space were removed", initialSize - 2, newStory.size)
        assertTrue(newStory[1] is GroupStep)
        assertEquals(
            "One element was added to the GroupStep",
            initialImageGroupSize + 1,
            (newStory[1] as GroupStep).steps.size
        )
    }

    @Test
    fun `it should be possible to merge an image outside a message group`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imageGroupRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories
        val initialGroupSize = (currentStory[1] as GroupStep).steps.size

        val positionTo = currentStory.size - 2
        val positionFrom = 1

        storyManager.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = (currentStory[positionFrom] as GroupStep).steps[0],
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "The image should be now in the position 3, because of spaces.",
            "group_image",
            newStory[3]!!.type
        )
        assertEquals(
            "The new story now it the GroupImage",
            initialGroupSize - 1,
            (newStory[1] as GroupStep).steps.size
        )
    }

    @Test
    fun `when moving outside of a group, the parent Id should be null now`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imageGroupRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories

        val positionTo = currentStory.size - 1
        val positionFrom = 1

        val storyToMove = (currentStory[positionFrom] as GroupStep).steps[0]

        storyManager.moveRequest(
            MoveInfo(
                storyUnit = storyToMove,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "The last StoryUnit should be an image.",
            "image",
            newStory[newStory.size - 2]!!.type
        )
        assertEquals(
            "The image should be in the correct place now.",
            storyToMove.id,
            newStory[newStory.size - 2]!!.id
        )
        assertNull(
            "The parent of the separated image, should not be there.",
            newStory[newStory.size - 2]!!.parentId
        )
        assertFalse(
            "The moved image should not be in the group anymore",
            (newStory[positionFrom] as GroupStep).steps.any { storyUnit ->
                storyUnit.id == storyToMove.id
            })
    }

    @Test
    fun `it should be possible to switch images places`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imagesInLineRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories
        val storyUnitToMove = currentStory[1]!!

        storyManager.moveRequest(
            MoveInfo(currentStory[1]!!, 1, 4)
        )

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "The first story should have been moved",
            newStory[3]!!.id,
            storyUnitToMove.id
        )
    }

    @Test
    fun `deleting and leave a single element in a group destroys the group`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(imageGroupRepo.history())

        val currentStory = storyManager.normalizedStepsState.value.stories
        assertEquals(
            "initial the story unit should be a group",
            "group_image",
            currentStory[1]!!.type
        )

        val lastImageInsideGroup = {
            (storyManager.normalizedStepsState.value.stories[1] as GroupStep)
                .steps
                .last()
        }

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

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "the group become just an image because there's only a single image",
            "image",
            newStory[1]!!.type
        )
    }

    @Test
    fun `when deleting a message it should not leave consecutive spaces`() = runTest {
        val storyManager = StoryTellerManager()
        storyManager.addStories(messagesRepo.history())

        storyManager.onDelete(
            DeleteInfo(
                storyManager.normalizedStepsState.value.stories[3]!!, 3
            )
        )

        val stack: Stack<StoryUnit> = Stack()

        storyManager.normalizedStepsState.value.stories.forEach { (_, storyUnit) ->
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
            storyManager.addStories(singleMessageRepo.history())

            val stories = storyManager.normalizedStepsState.value.stories
            val initialSize = stories.size

            storyManager.onLineBreak(LineBreakInfo(stories[1] as StoryStep, position = 1))

            assertEquals(
                "2 new stories should have been added",
                initialSize + 2,
                storyManager.normalizedStepsState.value.stories.size
            )
        }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - complex`() =
        runTest {
            val storyManager = StoryTellerManager()
            storyManager.addStories(messagesRepo.history())

            val stories = storyManager.normalizedStepsState.value.stories
            val initialSize = stories.size

            storyManager.onLineBreak(LineBreakInfo(stories[1] as StoryStep, 1))

            assertEquals(
                "2 new stories should have been added",
                initialSize + 2,
                storyManager.normalizedStepsState.value.stories.size
            )
        }

    @Test
    fun `Complex move case1`() = runTest {
        /**
         * Steps:
         * 1 - Make 3 single images into a group
         * - Check that the 3 images are in a group
         * 2 - Move one image away.
         * - Check that the correct image was moved correctly
         */
        val storyManager = StoryTellerManager()
        storyManager.addStories(complexMessagesRepository.history())

        val stories = storyManager.normalizedStepsState.value.stories

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

        val newStory = storyManager.normalizedStepsState.value.stories

        assertEquals("The images should have been merged", 2, (newStory[1] as GroupStep).steps.size)

        val stories2 = storyManager.normalizedStepsState.value.stories

        val positionTo2 = 1
        val positionFrom2 = 3
        storyManager.mergeRequest(
            MergeInfo(
                receiver = (stories2[positionTo2] as GroupStep).steps.first(),
                sender = stories2[positionFrom2]!!,
                positionFrom = positionFrom2,
                positionTo = positionTo2,
            )
        )

        val newStory2 = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "The images should have been merged",
            3,
            (newStory2[1] as GroupStep).steps.distinctBy { storyUnit -> storyUnit.localId }.size
        )

        val positionFrom3 = 1
        val positionTo3 = 4
        val storyToMove = (newStory[positionFrom3] as GroupStep).steps.first()
        storyManager.moveRequest(
            MoveInfo(
                storyUnit = storyToMove,
                positionFrom = positionFrom3,
                positionTo = positionTo3,
            )
        )

        val newStory3 = storyManager.normalizedStepsState.value.stories

        assertEquals(
            "One image should have been separated",
            2,
            (newStory3[1] as GroupStep).steps.size
        )
        assertEquals(
            "The correct StoryUnit should have been moved",
            storyToMove.id,
            newStory3[5]!!.id
        )
    }

}

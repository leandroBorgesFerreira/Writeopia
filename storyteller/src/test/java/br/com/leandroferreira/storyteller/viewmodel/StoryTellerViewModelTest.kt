package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.utils.ListStoryData
import br.com.leandroferreira.storyteller.utils.MainDispatcherRule
import br.com.leandroferreira.storyteller.utils.MapStoryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.Stack

@OptIn(ExperimentalCoroutinesApi::class)
class StoryTellerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val imagesInLineRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): List<StoryUnit> = ListStoryData.imagesInLine()
        override suspend fun historyMap(): Map<Int, StoryUnit> = MapStoryData.imageStepsList()
    }

    private val imageGroupRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): List<StoryUnit> = ListStoryData.imageGroup()
        override suspend fun historyMap(): Map<Int, StoryUnit> = MapStoryData.imageGroup()
    }

    private val messagesRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): List<StoryUnit> = ListStoryData.messagesInLine()
        override suspend fun historyMap(): Map<Int, StoryUnit> = MapStoryData.messagesInLine()
    }

    private val singleMessageRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): List<StoryUnit> = ListStoryData.singleMessage()
        override suspend fun historyMap(): Map<Int, StoryUnit> = MapStoryData.singleMessage()
    }


    @Test
    fun `one space has to be added between steps`() = runTest {
        val storyViewModel = StoryTellerViewModel(messagesRepo)

        val oldSize = messagesRepo.historyMap().size

        storyViewModel.requestHistoriesFromApi()

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

        assertEquals("a space should be added between each step", oldSize * 2 + 1, newStory.size)
    }

    @Test
    fun `merge request should work`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsStateMap.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 2
        val positionTo = 1
        val sender = currentStory[positionFrom]!!
        val receiver = currentStory[positionTo]!!

        assertTrue("The first step is not a GroupStep", currentStory[1] is StoryStep)

        storyViewModel.mergeRequest(
            MergeInfo(
                sender = sender,
                receiver = receiver,
                positionFrom = positionFrom,
                positionTo = positionTo,
            )
        )

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

        assertEquals(
            "One image should be removed and one space",
            initialSize - 2,
            newStory.size
        )
        assertTrue("The first step should be now a GroupStep", newStory[1] is GroupStep)
    }

    @Test
    fun `merge request should work2`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsStateMap.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 2
        val positionTo = 1

        storyViewModel.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

        assertEquals(initialSize - 2, newStory.size)
        assertTrue("The first image should be a GroupImage now", newStory[1] is GroupStep)
        assertTrue("Other images should still exist", newStory[3] is StoryStep)
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsStateMap.value.stories
        val initialSize = currentStory.size

        val positionFrom = currentStory.size - 2
        val positionTo = 1

        storyViewModel.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newHistory = storyViewModel.normalizedStepsStateMap.value.stories

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
            val newHistory2 = storyViewModel.normalizedStepsStateMap.value.stories

            val newPositionFrom = newHistory2.size - 2
            val newPositionTo = 1

            storyViewModel.mergeRequest(
                MergeInfo(
                    receiver = newHistory2[newPositionTo]!!,
                    sender = newHistory2[newPositionFrom]!!,
                    positionTo = newPositionTo,
                    positionFrom = newPositionFrom
                )
            )
        }

        val newHistory3 = storyViewModel.normalizedStepsStateMap.value.stories

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
        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsStateMap.value.stories
        val initialSize = currentStory.size
        val initialImageGroupSize = (currentStory[1] as GroupStep).steps.size

        val positionFrom = currentStory.size - 2
        val positionTo = 1

        storyViewModel.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

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
        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsStateMap.value.stories
        val initialGroupSize = (currentStory[1] as GroupStep).steps.size

        val positionTo = currentStory.size - 2
        val positionFrom = 1

        storyViewModel.mergeRequest(
            MergeInfo(
                receiver = currentStory[positionTo]!!,
                sender = (currentStory[positionFrom] as GroupStep).steps[0],
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

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
    fun `it should be possible to switch images places`() {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsStateMap.value.stories
        val storyUnitToMove = currentStory[1]!!

        storyViewModel.moveRequest(
            MoveInfo(currentStory[1]!!, 1, 4)
        )

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

        assertEquals(
            "The first story should have been moved",
            newStory[3]!!.id,
            storyUnitToMove.id
        )
    }

    @Test
    fun `deleting and leave a single element in a group destroys the group`() {
        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsStateMap.value.stories
        assertEquals(
            "initial the story unit should be a group",
            "group_image",
            currentStory[1]!!.type
        )

        val lastImageInsideGroup = {
            (storyViewModel.normalizedStepsStateMap.value.stories[1] as GroupStep)
                .steps
                .last()
        }

        storyViewModel.onDelete(
            DeleteInfo(
                storyUnit = lastImageInsideGroup(),
                position = 1
            )
        )

        storyViewModel.onDelete(
            DeleteInfo(
                storyUnit = lastImageInsideGroup(),
                position = 1
            )
        )

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

        assertEquals(
            "the group become just an image because there's only a single image",
            "image",
            newStory[1]!!.type
        )
    }

    @Test
    fun `when deleting a message it should not leave consecutive spaces`() {
        val storyViewModel = StoryTellerViewModel(messagesRepo)
        storyViewModel.requestHistoriesFromApi()

        storyViewModel.onDelete(
            DeleteInfo(
                storyViewModel.normalizedStepsStateMap.value.stories[3]!!, 3
            )
        )

        val stack: Stack<StoryUnit> = Stack()

        storyViewModel.normalizedStepsStateMap.value.stories.forEach { (_, storyUnit) ->
            if (stack.isNotEmpty() && stack.peek().type == "space" && storyUnit.type == "space") {
                fail("Consecutive spaces happened.")
            }

            stack.add(storyUnit)
        }
    }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - simple`() {
        val storyViewModel = StoryTellerViewModel(singleMessageRepo)
        storyViewModel.requestHistoriesFromApi()

        val stories = storyViewModel.normalizedStepsStateMap.value.stories
        val initialSize = stories.size

        storyViewModel.onLineBreak(stories[1] as StoryStep)

        assertEquals(
            "2 new stories should have been added",
            initialSize + 2,
            storyViewModel.normalizedStepsStateMap.value.stories.size
        )
    }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - complex`() {
        val storyViewModel = StoryTellerViewModel(messagesRepo)
        storyViewModel.requestHistoriesFromApi()

        val stories = storyViewModel.normalizedStepsStateMap.value.stories
        val initialSize = stories.size

        storyViewModel.onLineBreak(stories[1] as StoryStep)

        assertEquals(
            "2 new stories should have been added",
            initialSize + 2,
            storyViewModel.normalizedStepsStateMap.value.stories.size
        )
    }

}

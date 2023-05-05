package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.utils.MainDispatcherRule
import br.com.leandroferreira.storyteller.utils.ListStoryData
import br.com.leandroferreira.storyteller.utils.MapStoryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

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

        val sender = currentStory[currentStory.size - 2]!!
        val receiver = currentStory[1]!!

        storyViewModel.mergeRequest(
            MergeInfo(
                sender = sender,
                receiver = receiver
            )
        )

        val newStory = storyViewModel.normalizedStepsStateMap.value.stories

        assertEquals(
            "One image should be removed and one space",
            initialSize - 2,
            newStory.size
        )
        assertTrue(newStory[1] is GroupStep)
    }

//    @Test
//    fun `merge request should work2`() = runTest {
//        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val currentStory = storyViewModel.normalizedStepsState.value.stories.toList()
//        val initialSize = currentStory.size
//
//        storyViewModel.mergeRequest(
//            receiverId = currentStory[1].id,
//            currentStory[currentStory.lastIndex - 1].id
//        )
//
//        val newStory = storyViewModel.normalizedStepsState.value.stories
//
//        assertEquals(initialSize - 2, newStory.size)
//        assertTrue("The first image should be a GroupImage now", newStory[1] is GroupStep)
//        assertTrue("Other images should still exist", newStory[3] is StoryStep)
//
//        newStory.forEachIndexed { index, storyUnit ->
//            assertEquals(index, storyUnit.localPosition)
//        }
//    }
//
//    @Test
//    @Ignore("To fix!")
//    fun `the merge must be idempotent`() = runTest {
//        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val currentStory = storyViewModel.normalizedStepsState.value.stories.toList()
//        val initialSize = currentStory.size
//
//        val firstItemId = currentStory[1].id
//        val lastItemId = currentStory[currentStory.lastIndex - 1].id
//
//        storyViewModel.mergeRequest(receiverId = firstItemId, senderId = lastItemId)
//        val newStory = storyViewModel.normalizedStepsState.value.stories
//
//        assertEquals(initialSize - 2, newStory.size)
//        assertTrue(newStory[1] is GroupStep)
//        // The last item was moved to the last position of a GroupStep
//        assertEquals((newStory[1] as GroupStep).steps.last().id, lastItemId)
//
//        newStory.forEachIndexed { index, storyUnit ->
//            assertEquals(index, storyUnit.localPosition)
//        }
//
//        /*
//         If a second merge exactly the same is asked, nothing should happen, because the merge
//         already happened.
//         */
//        storyViewModel.mergeRequest(receiverId = firstItemId, senderId = lastItemId)
//
//        val newStory2 = storyViewModel.normalizedStepsState.value.stories
//
//        assertEquals(initialSize - 2, newStory2.size)
//        assertTrue(newStory2[1] is GroupStep)
//        // The last item was moved to the last position of a GroupStep
//        assertEquals((newStory2[1] as GroupStep).steps.last().id, lastItemId)
//
//        newStory2.forEachIndexed { index, storyUnit ->
//            assertEquals(index, storyUnit.localPosition)
//        }
//    }
//
//    @Test
//    fun `multiple merge requests should work`() = runTest {
//        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val currentStory = storyViewModel.normalizedStepsState.value.stories.toList()
//        val initialSize = currentStory.size
//
//        storyViewModel.mergeRequest(
//            receiverId = currentStory[1].id,
//            senderId = currentStory[currentStory.lastIndex - 1].id
//        )
//
//        val newHistory = storyViewModel.normalizedStepsState.value.stories.toList()
//
//        assertEquals("One space and one image were removed", initialSize - 2, newHistory.size)
//        assertTrue(
//            "The first message should be a GroupImage instead of a Image now",
//            newHistory[1] is GroupStep
//        )
//        assertEquals(
//            "The new created GroupImage should have 2 images",
//            2,
//            (newHistory[1] as GroupStep).steps.size
//        )
//
//        storyViewModel.mergeRequest(
//            receiverId = newHistory[1].id,
//            senderId = newHistory[newHistory.lastIndex - 1].id
//        )
//
//        val newHistory2 = storyViewModel.normalizedStepsState.value.stories
//
//        assertEquals(initialSize - 4, newHistory2.size)
//        assertTrue("The GroupImage should still exist", newHistory2[1] is GroupStep)
//        assertEquals("Now the group has 3 images", 3, (newHistory2[1] as GroupStep).steps.size)
//    }
//
//    @Test
//    fun `it should be possible to merge an image inside a message group`() = runTest {
//        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val currentStory = storyViewModel.normalizedStepsState.value.stories.toList()
//        val initialSize = currentStory.size
//        val initialImageGroupSize = (currentStory[1] as GroupStep).steps.size
//
//        storyViewModel.mergeRequest(
//            receiverId = currentStory[1].id,
//            senderId = currentStory[currentStory.lastIndex - 1].id
//        )
//
//        val newStory = storyViewModel.normalizedStepsState.value.stories
//
//        assertEquals("One image and one space were removed", initialSize - 2, newStory.size)
//        assertTrue(newStory[1] is GroupStep)
//        assertEquals(
//            "One element was added to the GroupStep",
//            initialImageGroupSize + 1,
//            (newStory[1] as GroupStep).steps.size
//        )
//
//        newStory.forEachIndexed { index, storyUnit ->
//            assertEquals(
//                "Each story unit should have the correct position",
//                index,
//                storyUnit.localPosition
//            )
//        }
//    }
//
//    @Test
//    fun `it should be possible to merge an image outside a message group`() = runTest {
//        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val currentStory = storyViewModel.normalizedStepsState.value.stories
//        val initialGroupSize = (currentStory[1] as GroupStep).steps.size
//
//        storyViewModel.moveRequest(
//            unitId = "1",
//            newPosition = 2
//        )
//
//        val newStory = storyViewModel.normalizedStepsState.value.stories
//
//        assertEquals(
//            "The image should be now in the position 3, because of spaces.",
//            "image",
//            newStory[3].type
//        )
//        assertEquals(
//            "The new story now it the GroupImage",
//            initialGroupSize - 1,
//            (newStory[1] as GroupStep).steps.size
//        )
//    }
//
//    @Test
//    fun `it should be possible to switch images places`() {
//        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        storyViewModel.moveRequest(
//            unitId = "1",
//            newPosition = 4
//        )
//
//        val newStory = storyViewModel.normalizedStepsState.value.stories
//
//        assertEquals("The history 4 should have been moved", newStory[3].id, "1")
//
//        newStory.reduce { acc, storyUnit ->
//            if (acc.type == "space" && storyUnit.type == "space") {
//                fail("No duplicated elements are accepted")
//            }
//
//            storyUnit
//        }
//    }
//
//    @Test
//    fun `deleting and leave a single element in a group destroys the group`() {
//        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val currentStory = storyViewModel.normalizedStepsState.value.stories.toList()
//        assertEquals(
//            "initial the story unit should be a group",
//            "group_image",
//            currentStory[1].type
//        )
//
//        storyViewModel.onListCommand(
//            Command(
//                "delete",
//                (storyViewModel.normalizedStepsState.value.stories.toList()[1] as GroupStep).steps[1]
//            )
//        )
//
//        storyViewModel.onListCommand(
//            Command(
//                "delete",
//                (storyViewModel.normalizedStepsState.value.stories.toList()[1] as GroupStep).steps[1]
//            )
//        )
//
//        val newStory = storyViewModel.normalizedStepsState.value.stories.toList()
//
//        assertEquals(
//            "the group become just an image because there's only a single image",
//            "image",
//            newStory[1].type
//        )
//    }
//
//    @Test
//    fun `when deleting a message it should not leave consecutive spaces`() {
//        val storyViewModel = StoryTellerViewModel(messagesRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        storyViewModel.onListCommand(
//            Command(
//                "delete",
//                storyViewModel.normalizedStepsState.value.stories.toList()[3]
//            )
//        )
//
//        val stack: Stack<StoryUnit> = Stack()
//
//        storyViewModel.normalizedStepsState.value.stories.forEach { storyUnit ->
//            if (stack.isNotEmpty() && stack.peek().type == "space" && storyUnit.type == "space") {
//                fail("Consecutive spaces happened.")
//            }
//
//            stack.add(storyUnit)
//        }
//    }
//
//    @Test
//    fun `when a line break happens, a new story unit with the same type should be created - simple`() {
//        val storyViewModel = StoryTellerViewModel(singleMessageRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val stories = storyViewModel.normalizedStepsState.value.stories
//        val initialSize = stories.size
//
//        storyViewModel.onLineBreak(stories[1] as StoryStep)
//
//        assertEquals(
//            "2 new stories should have been added",
//            initialSize + 2,
//            storyViewModel.normalizedStepsState.value.stories.size
//        )
//    }
//
//    @Test
//    fun `when a line break happens, a new story unit with the same type should be created - complex`() {
//        val storyViewModel = StoryTellerViewModel(messagesRepo)
//        storyViewModel.requestHistoriesFromApi()
//
//        val stories = storyViewModel.normalizedStepsState.value.stories
//        val initialSize = stories.size
//
//        storyViewModel.onLineBreak(stories[1] as StoryStep)
//
//        assertEquals(
//            "2 new stories should have been added",
//            initialSize + 2,
//            storyViewModel.normalizedStepsState.value.stories.size
//        )
//    }

}

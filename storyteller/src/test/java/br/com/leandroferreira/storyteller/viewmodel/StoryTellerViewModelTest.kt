package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.Command
import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.utils.MainDispatcherRule
import br.com.leandroferreira.storyteller.utils.StoryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StoryTellerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val imagesInLineRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): List<StoryUnit> = StoryData.imagesInLine()
    }

    private val imageGroupRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): List<StoryUnit> = StoryData.imageGroup()
    }

    @Test
    fun `merge request should be able to be initialized`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        assertTrue(storyViewModel.normalizedStepsState.value.isNotEmpty())
    }

    @Test
    fun `merge request should work`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values.toList()
        val initialSize = currentStory.size

        storyViewModel.mergeRequest(
            receiverId = currentStory[1].id,
            senderId = currentStory[currentStory.lastIndex - 1].id
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(
            "One image should be removed and one space",
            initialSize - 2,
            newStory.size
        )
        assertTrue(newStory[1] is GroupStep)

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }

    @Test
    fun `merge request should work2`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values.toList()
        val initialSize = currentStory.size

        storyViewModel.mergeRequest(
            receiverId = currentStory[1].id,
            currentStory[currentStory.lastIndex - 1].id
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 2, newStory.size)
        assertTrue("The first image should be a GroupImage now", newStory[1] is GroupStep)
        assertTrue("Other images should still exist", newStory[3] is StoryStep)

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }

    @Test
    @Ignore("To fix!")
    fun `the merge must be idempotent`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values.toList()
        val initialSize = currentStory.size

        val firstItemId = currentStory[1].id
        val lastItemId = currentStory[currentStory.lastIndex - 1].id

        storyViewModel.mergeRequest(receiverId = firstItemId, senderId = lastItemId)
        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 2, newStory.size)
        assertTrue(newStory[1] is GroupStep)
        // The last item was moved to the last position of a GroupStep
        assertEquals((newStory[1] as GroupStep).steps.last().id, lastItemId)

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }

        /*
         If a second merge exactly the same is asked, nothing should happen, because the merge
         already happened.
         */
        storyViewModel.mergeRequest(receiverId = firstItemId, senderId = lastItemId)

        val newStory2 = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 2, newStory2.size)
        assertTrue(newStory2[1] is GroupStep)
        // The last item was moved to the last position of a GroupStep
        assertEquals((newStory2[1] as GroupStep).steps.last().id, lastItemId)

        newStory2.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val storyViewModel = StoryTellerViewModel(imagesInLineRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values.toList()
        val initialSize = currentStory.size

        storyViewModel.mergeRequest(
            receiverId = currentStory[1].id,
            senderId = currentStory[currentStory.lastIndex - 1].id
        )

        val newHistory = storyViewModel.normalizedStepsState.value.values.toList()

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

        storyViewModel.mergeRequest(
            receiverId = newHistory[1].id,
            senderId = newHistory[newHistory.lastIndex - 1].id
        )

        val newHistory2 = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 4, newHistory2.size)
        assertTrue("The GroupImage should still exist", newHistory2[1] is GroupStep)
        assertEquals("Now the group has 3 images", 3, (newHistory2[1] as GroupStep).steps.size)
    }

    @Test
    fun `it should be possible to merge an image inside a message group`() = runTest {
        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values.toList()
        val initialSize = currentStory.size
        val initialImageGroupSize = (currentStory[1] as GroupStep).steps.size

        storyViewModel.mergeRequest(
            receiverId = currentStory[1].id,
            senderId = currentStory[currentStory.lastIndex - 1].id
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals("One image and one space were removed", initialSize - 2, newStory.size)
        assertTrue(newStory[1] is GroupStep)
        assertEquals(
            "One element was added to the GroupStep",
            initialImageGroupSize + 1,
            (newStory[1] as GroupStep).steps.size
        )

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(
                "Each story unit should have the correct position",
                index,
                storyUnit.localPosition
            )
        }
    }

    @Test
    fun `it should be possible to merge an image outside a message group`() = runTest {
        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value
        val initialGroupSize = (currentStory[1] as GroupStep).steps.size

        storyViewModel.moveRequest(
            unitId = "1",
            newPosition = 2
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(
            "The image should be now in the position 3, because of spaces.",
            "image",
            newStory[3]?.type
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

        storyViewModel.moveRequest(
            unitId = "1",
            newPosition = 4
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals("The history 4 should have been moved", newStory[3]!!.id, "1")

        newStory.values.reduce { acc, storyUnit ->
            if (acc.type == "space" && storyUnit.type == "space") {
                fail("No duplicated elements are accepted")
            }

            storyUnit
        }
    }

    @Test
    fun `deleting and leave a single element in a group destroys the group`() {
        val storyViewModel = StoryTellerViewModel(imageGroupRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values.toList()
        assertEquals("initial the story unit should be a group", "group_image", currentStory[1].type)

        storyViewModel.onListCommand(
            Command(
                "delete",
                (storyViewModel.normalizedStepsState.value.values.toList()[1] as GroupStep).steps[1]
            )
        )

        storyViewModel.onListCommand(
            Command(
                "delete",
                (storyViewModel.normalizedStepsState.value.values.toList()[1] as GroupStep).steps[1]
            )
        )

        val newStory = storyViewModel.normalizedStepsState.value.values.toList()

        assertEquals(
            "the group become just an image because there's only a single image",
            "image",
            newStory[1].type
        )
    }
}

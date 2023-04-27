package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.normalization.UnchangedNormalizer
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.utils.MainDispatcherRule
import br.com.leandroferreira.storyteller.utils.StoryData
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
    fun `merge request should work - without spaces`() = runTest {
        val history = imagesInLineRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(
            imagesInLineRepo,
            spacesNormalizer = UnchangedNormalizer::skipChange
        )
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values

        storyViewModel.mergeRequest(
            receiverId = currentStory.first().id,
            senderId = currentStory.last().id
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 1, newStory.size)
        assertTrue(newStory[0] is GroupStep)
        assertTrue(newStory[1] is StoryStep)
        assertEquals(newStory[1], history[1])
        assertEquals(1, newStory[1]?.localPosition)

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }

    @Test
    fun `merge request should work2 - without spaces`() = runTest {
        val history = imagesInLineRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(
            imagesInLineRepo,
            spacesNormalizer = UnchangedNormalizer::skipChange
        )
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values

        storyViewModel.mergeRequest(
            receiverId = currentStory.first().id,
            currentStory.toList()[1].id
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 1, newStory.size)
        assertTrue(newStory[0] is GroupStep)
        assertTrue(newStory[1] is StoryStep)
        assertEquals(newStory[1]?.id, history[2].id)
        assertEquals(1, newStory[1]?.localPosition)

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }

    @Test
    fun `the merge must be idempotent`() = runTest {
        val history = imagesInLineRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(
            imagesInLineRepo,
            spacesNormalizer = UnchangedNormalizer::skipChange
        )
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values

        val firstItemId = currentStory.first().id
        val lastItemId = currentStory.last().id

        storyViewModel.mergeRequest(receiverId = firstItemId, senderId = lastItemId)

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 1, newStory.size)
        assertTrue(newStory[0] is GroupStep)
        // The last item was moved to the last position of a GroupStep
        assertEquals((newStory[0] as GroupStep).steps.last().id, lastItemId)

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }

        /*
         If a second merge exactly the same is asked, nothing should happen, because the merge
         already happened.
         */
        storyViewModel.mergeRequest(receiverId = firstItemId, senderId = lastItemId)

        val newStory2 = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 1, newStory2.size)
        assertTrue(newStory[0] is GroupStep)
        // The last item was moved to the last position of a GroupStep
        assertEquals((newStory[0] as GroupStep).steps.last().id, lastItemId)

        newStory2.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val history = imagesInLineRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(
            imagesInLineRepo,
            spacesNormalizer = UnchangedNormalizer::skipChange
        )
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values

        storyViewModel.mergeRequest(
            receiverId = currentStory.first().id,
            senderId = currentStory.last().id
        )


        val newHistory = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 1, newHistory.size)
        assertTrue(newHistory[0] is GroupStep)
        assertEquals(2, (newHistory[0] as GroupStep).steps.size)


        storyViewModel.mergeRequest(
            receiverId = newHistory.values.first().id,
            senderId = newHistory.values.last().id
        )

        val newHistory2 = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 2, newHistory2.size)
        assertTrue(newHistory2[0] is GroupStep)
        assertEquals(3, (newHistory2[0] as GroupStep).steps.size)
        assertTrue(newHistory2[1] is StoryStep)
    }

    @Test
    fun `it should be possible to merge an image inside a message group`() = runTest {
        val storyViewModel = StoryTellerViewModel(
            imageGroupRepo,
            spacesNormalizer = UnchangedNormalizer::skipChange
        )
        storyViewModel.requestHistoriesFromApi()

        val initialSize = (imageGroupRepo.history()[0] as GroupStep).steps.size

        val currentStory = storyViewModel.normalizedStepsState.value.values

        storyViewModel.mergeRequest(
            receiverId = currentStory.first().id,
            senderId = currentStory.last().id
        )

        val newStory = storyViewModel.normalizedStepsState.value

        assertEquals(1, newStory.size)
        assertTrue(newStory[0] is GroupStep)
        assertEquals(initialSize + 1, (newStory[0] as GroupStep).steps.size)

        newStory.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
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

        assertEquals("image", newStory[2]?.type)
        assertEquals(initialGroupSize - 1, (newStory[1] as GroupStep).steps.size)
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

        assertEquals("The history 4 should have been moved", newStory[4]!!.id, "1")
    }
}

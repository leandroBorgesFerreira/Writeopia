package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
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

    private val mockRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): List<StoryUnit> = StoryData.imagesInLine()
    }

    @Test
    fun `merge request should be able to be initialized`() = runTest {
        val storyViewModel = StoryTellerViewModel(mockRepo)
        storyViewModel.requestHistoriesFromApi()

        assertTrue(storyViewModel.normalizedStepsState.value.isNotEmpty())
    }

    @Test
    fun `merge request should work`() = runTest {
        val history = mockRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(mockRepo)
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
    fun `merge request should work2`() = runTest {
        val history = mockRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(mockRepo)
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
        val history = mockRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(mockRepo)
        storyViewModel.requestHistoriesFromApi()

        val currentStory = storyViewModel.normalizedStepsState.value.values

        storyViewModel.mergeRequest(
            receiverId = currentStory.first().id,
            senderId = currentStory.toList()[1].id
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

        /*
         If a second merge exactly the same is asked, nothing should happen, because the merge
         already happened.
         */
        storyViewModel.mergeRequest(
            receiverId = currentStory.first().id,
            senderId = currentStory.toList()[1].id
        )

        val newStory2 = storyViewModel.normalizedStepsState.value

        assertEquals(initialSize - 1, newStory2.size)
        assertTrue(newStory2[0] is GroupStep)
        assertTrue(newStory2[1] is StoryStep)
        assertEquals(newStory2[1]?.id, history[2].id)
        assertEquals(1, newStory2[1]?.localPosition)

        newStory2.values.forEachIndexed { index, storyUnit ->
            assertEquals(index, storyUnit.localPosition)
        }
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val history = mockRepo.history()
        val initialSize = history.size

        val storyViewModel = StoryTellerViewModel(mockRepo)
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
}

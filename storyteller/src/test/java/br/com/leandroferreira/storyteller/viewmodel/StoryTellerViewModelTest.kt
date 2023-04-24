package br.com.leandroferreira.storyteller.viewmodel

import br.com.leandroferreira.storyteller.model.GroupStep
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

        val storyViewModel = StoryTellerViewModel(mockRepo)
        storyViewModel.requestHistoriesFromApi()

        assertEquals(3, storyViewModel.normalizedStepsState.value.size)

        storyViewModel.mergeRequest(receiving = history.first(), history.last())

        assertEquals(2, storyViewModel.normalizedStepsState.value.size)
        assertTrue(storyViewModel.normalizedStepsState.value[0] is GroupStep)
    }
}

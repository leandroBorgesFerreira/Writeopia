package io.writeopia.note_menu.viewmodel

import io.mockk.coEvery
import io.mockk.mockk
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.repository.UiConfigurationSqlDelightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlin.test.*
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChooseNoteViewModelTest {

    private val notesUseCase: NotesUseCase = mockk()
    private val notesConfig: ConfigurationRepository = mockk()
    private val authManager: AuthManager = mockk()
    private val uiConfigurationSqlDelightRepository: UiConfigurationSqlDelightRepository = mockk()

    @Test
    @Ignore("Todo: Fix")
    fun `after deleting a document the selection should be reset`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            coEvery { notesUseCase.deleteNotes(any()) } returns Unit

            val viewModel = ChooseNoteKmpViewModel(
                notesUseCase,
                notesConfig,
                uiConfigurationSqlDelightRepository,
                authManager,
                MutableStateFlow(false)
            ).apply {
                initCoroutine(this@runTest)
            }

            val selectedNotesList = mutableListOf<Boolean>()
            backgroundScope.launch(testDispatcher) {
                viewModel.hasSelectedNotes.toList(selectedNotesList)
            }

            repeat(5) { i ->
                viewModel.onDocumentSelected("$i", true)
            }
            viewModel.deleteSelectedNotes()

            advanceUntilIdle()

            assertEquals(selectedNotesList[0], false)
            assertEquals(selectedNotesList[1], true)
            assertEquals(selectedNotesList[2], false)
            assertFalse(viewModel.hasSelectedNotes.value)
        } finally {
            Dispatchers.resetMain()
        }
    }

}

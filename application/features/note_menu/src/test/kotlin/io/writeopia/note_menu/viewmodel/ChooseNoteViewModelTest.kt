package io.writeopia.note_menu.viewmodel

import io.mockk.coEvery
import io.mockk.mockk
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.usecase.NotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ChooseNoteViewModelTest {

    private val notesUseCase: NotesUseCase = mockk()
    private val notesConfig: NotesConfigurationRepository = mockk()
    private val authManager: AuthManager = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `it should be possible to select documents`() = runTest {
        val viewModel = ChooseNoteViewModel(notesUseCase, notesConfig, authManager)

        repeat(5) { i ->
            viewModel.onDocumentSelected("$i", true)
        }

        val selectedNotesList = mutableListOf<Boolean>()

        backgroundScope.launch(testDispatcher) {
            viewModel.hasSelectedNotes.toList(selectedNotesList)
        }

        assertEquals(selectedNotesList.size, 2)
        assertTrue(viewModel.hasSelectedNotes.value)
    }

    @Test
    @Ignore("Test fails when running with other tests")
    fun `after deleting a document the selection should be reset `() = runTest {
        coEvery { notesUseCase.deleteNotes(any()) } returns Unit

        val viewModel = ChooseNoteViewModel(notesUseCase, notesConfig, authManager)

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
    }
}

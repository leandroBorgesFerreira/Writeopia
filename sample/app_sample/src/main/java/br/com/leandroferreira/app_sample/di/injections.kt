package br.com.leandroferreira.app_sample.di

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.leandroferreira.editor.NoteDetailsViewModel
import br.com.leandroferreira.note_menu.data.usecase.NotesConfigurationRepository
import br.com.leandroferreira.note_menu.data.usecase.NotesUseCase
import br.com.leandroferreira.note_menu.viewmodel.ChooseNoteViewModel
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl

class NotesInjection(
    private val database: StoryTellerDatabase,
    private val sharedPreferences: SharedPreferences
) {

    private fun provideStoryTellerManager() = StoryTellerManager()

    private fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )

    private fun provideNotesConfigurationRepository(): NotesConfigurationRepository =
        NotesConfigurationRepository(sharedPreferences)

    private fun provideNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        notesConfigurationRepository: NotesConfigurationRepository =
            provideNotesConfigurationRepository()
    ): NotesUseCase {
        return NotesUseCase(documentRepository, notesConfigurationRepository)
    }

    @Composable
    internal fun provideChooseNoteViewModel(
        notesUseCase: NotesUseCase = provideNotesUseCase(),
        notesConfig: NotesConfigurationRepository = provideNotesConfigurationRepository()
    ): ChooseNoteViewModel {
        return viewModel(factory = ChooseNoteViewModelFactory(notesUseCase, notesConfig))
    }

    @Composable
    internal fun provideNoteDetailsViewModel(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        storyTellerManager: StoryTellerManager = provideStoryTellerManager()
    ): NoteDetailsViewModel {
        return viewModel(initializer = {
            NoteDetailsViewModel(storyTellerManager, documentRepository)
        })
    }
}

package com.github.leandroborgesferreira.storytellerapp.note_menu.di

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesConfigurationRepository
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesUseCase
import com.github.leandroborgesferreira.storytellerapp.note_menu.viewmodel.ChooseNoteViewModel

class NotesMenuInjection(
    private val database: StoryTellerDatabase,
    private val sharedPreferences: SharedPreferences,
) {

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
}

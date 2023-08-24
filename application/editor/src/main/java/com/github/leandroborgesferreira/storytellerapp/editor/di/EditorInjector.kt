package com.github.leandroborgesferreira.storytellerapp.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl
import com.github.leandroborgesferreira.storyteller.persistence.tracker.OnUpdateDocumentTracker
import com.github.leandroborgesferreira.storytellerapp.editor.NoteEditorViewModel

class EditorInjector(
    private val database: StoryTellerDatabase,
) {

    private fun provideStoryTellerManager() = StoryTellerManager(
        documentTracker = OnUpdateDocumentTracker(provideDocumentRepository())
    )

    private fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )

    @Composable
    internal fun provideNoteDetailsViewModel(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        storyTellerManager: StoryTellerManager = provideStoryTellerManager()
    ): NoteEditorViewModel {
        return viewModel(initializer = {
            NoteEditorViewModel(storyTellerManager, documentRepository)
        })
    }
}
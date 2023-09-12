package com.storiesteller.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.storiesteller.sdk.manager.DocumentRepository
import com.storiesteller.sdk.manager.StoriesTellerManager
import com.storiesteller.sdk.persistence.database.StoriesTellerDatabase
import com.storiesteller.sdk.persistence.repository.DocumentRepositoryImpl
import com.storiesteller.sdk.persistence.tracker.OnUpdateDocumentTracker
import com.storiesteller.auth.core.AuthManager
import com.storiesteller.auth.core.di.AuthCoreInjection
import com.storiesteller.editor.NoteEditorViewModel

class EditorInjector(
    private val database: StoriesTellerDatabase,
    private val authCoreInjection: AuthCoreInjection,
) {

    private fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )


    private fun provideStoriesTellerManager(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ) =
        StoriesTellerManager(
            documentTracker = OnUpdateDocumentTracker(documentRepository),
            userId = { authManager.getUser().id }
        )

    @Composable
    internal fun provideNoteDetailsViewModel(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        storiesTellerManager: StoriesTellerManager = provideStoriesTellerManager()
    ): NoteEditorViewModel {
        return viewModel(initializer = {
            NoteEditorViewModel(storiesTellerManager, documentRepository)
        })
    }
}
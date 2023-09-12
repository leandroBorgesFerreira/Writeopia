package io.storiesteller.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.storiesteller.sdk.manager.DocumentRepository
import io.storiesteller.sdk.manager.StoriesTellerManager
import io.storiesteller.sdk.persistence.database.StoriesTellerDatabase
import io.storiesteller.sdk.persistence.repository.DocumentRepositoryImpl
import io.storiesteller.sdk.persistence.tracker.OnUpdateDocumentTracker
import io.storiesteller.auth.core.AuthManager
import io.storiesteller.auth.core.di.AuthCoreInjection
import io.storiesteller.editor.NoteEditorViewModel

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
package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.persistence.database.WriteopiaDatabase
import io.writeopia.sdk.persistence.repository.DocumentRepositoryImpl
import io.writeopia.sdk.persistence.tracker.OnUpdateDocumentTracker
import io.writeopia.auth.core.AuthManager
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.editor.NoteEditorViewModel

class EditorInjector(
    private val database: WriteopiaDatabase,
    private val authCoreInjection: AuthCoreInjection,
) {

    private fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )


    private fun provideWriteopiaManager(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ) =
        WriteopiaManager(
            documentTracker = OnUpdateDocumentTracker(documentRepository),
            userId = { authManager.getUser().id }
        )

    @Composable
    internal fun provideNoteDetailsViewModel(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        writeopiaManager: WriteopiaManager = provideWriteopiaManager()
    ): NoteEditorViewModel {
        return viewModel(initializer = {
            NoteEditorViewModel(writeopiaManager, documentRepository)
        })
    }
}
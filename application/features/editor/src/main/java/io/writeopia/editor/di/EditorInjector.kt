package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.persistence.repository.DocumentRepositoryImpl
import io.writeopia.auth.core.AuthManager
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.editor.NoteEditorViewModel
import io.writeopia.persistence.WriteopiaApplicationDatabase

class EditorInjector(
    private val database: WriteopiaApplicationDatabase,
    private val authCoreInjection: AuthCoreInjection,
) {

    private fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )


    private fun provideWriteopiaManager(
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ) = WriteopiaManager(
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

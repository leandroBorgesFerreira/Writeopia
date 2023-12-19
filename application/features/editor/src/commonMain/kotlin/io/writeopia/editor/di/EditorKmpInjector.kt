package io.writeopia.editor.di

import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.editor.viewmodel.NoteEditorKmpViewModel
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import kotlinx.coroutines.Dispatchers

class EditorKmpInjector(
    private val authCoreInjection: AuthCoreInjection,
    private val repositoryInjection: RepositoryInjector
) {

    private fun provideDocumentRepository(): DocumentRepository =
        repositoryInjection.provideDocumentRepository()

    private fun provideWriteopiaManager(): WriteopiaManager = WriteopiaManager()

    private fun provideWriteopiaStateManager(
        authManager: AuthManager = authCoreInjection.provideAccountManager(),
        writeopiaManager: WriteopiaManager = provideWriteopiaManager()
    ) = WriteopiaStateManager.create(
        userId = { authManager.getUser().id },
        dispatcher = Dispatchers.IO,
        writeopiaManager = writeopiaManager
    )

    fun provideNoteEditorViewModel(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        writeopiaManager: WriteopiaStateManager = provideWriteopiaStateManager(),
    ): NoteEditorKmpViewModel = NoteEditorKmpViewModel(writeopiaManager, documentRepository)
}

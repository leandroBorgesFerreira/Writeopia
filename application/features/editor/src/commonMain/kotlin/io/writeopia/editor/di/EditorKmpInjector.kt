package io.writeopia.editor.di

import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.editor.viewmodel.NoteEditorKmpViewModel
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import kotlinx.coroutines.Dispatchers

class EditorKmpInjector(
    private val authCoreInjection: AuthCoreInjection,
    private val repositoryInjection: RepositoryInjector
) {

    private fun provideDocumentRepository(): DocumentRepository =
        repositoryInjection.provideDocumentRepository()

    private fun provideWriteopiaManager(
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ) = WriteopiaManager(
        userId = { authManager.getUser().id },
        dispatcher = Dispatchers.IO
    )

    fun provideNoteDetailsViewModel(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        writeopiaManager: WriteopiaManager = provideWriteopiaManager(),
    ): NoteEditorKmpViewModel = NoteEditorKmpViewModel(writeopiaManager, documentRepository)
}

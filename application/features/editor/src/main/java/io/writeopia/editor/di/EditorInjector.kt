package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.editor.NoteEditorViewModel
import io.writeopia.persistence.injection.RepositoriesInjection
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.manager.WriteopiaManager
import kotlinx.coroutines.Dispatchers

class EditorInjector(
    private val authCoreInjection: AuthCoreInjection,
    private val repositoriesInjection: RepositoriesInjection
) {

    private fun provideDocumentRepository(): DocumentRepository =
        repositoriesInjection.provideDocumentRepository()

    private fun provideWriteopiaManager(
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ) = WriteopiaManager(
        userId = { authManager.getUser().id },
        dispatcher = Dispatchers.IO
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

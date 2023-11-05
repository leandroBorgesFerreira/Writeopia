package io.writeopia.editor.di

import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.editor.viewmodel.NoteEditorKmpViewModel
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.sdk.persistence.core.di.DaosInjector
import kotlinx.coroutines.Dispatchers

internal class EditorKmpInjector(
    private val authCoreInjection: AuthCoreInjection,
    private val daosInjection: DaosInjector
) {

    private fun provideDocumentRepository(): DocumentDao =
        daosInjection.provideDocumentDao()

    private fun provideWriteopiaManager(
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ) = WriteopiaManager(
        userId = { authManager.getUser().id },
        dispatcher = Dispatchers.IO
    )

    internal fun provideNoteDetailsViewModel(
        documentDao: DocumentDao = provideDocumentRepository(),
        writeopiaManager: WriteopiaManager = provideWriteopiaManager()
    ): NoteEditorKmpViewModel = NoteEditorKmpViewModel(writeopiaManager, documentDao)
}

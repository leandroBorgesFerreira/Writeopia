package io.writeopia.note_menu.di

import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.note_menu.data.repository.NotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteKmpViewModel
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.sdk.persistence.core.dao.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import kotlinx.coroutines.CoroutineScope

class NotesMenuKmpInjection(
    private val notesConfigurationInjector: NotesConfigurationInjector,
    private val authCoreInjection: AuthCoreInjection,
    private val repositoryInjection: RepositoryInjector
) {

    private fun provideDocumentRepository(): DocumentRepository =
        repositoryInjection.provideDocumentRepository()

    private fun provideNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        notesConfigurationRepository: NotesConfigurationRepository =
            notesConfigurationInjector.provideNotesConfigurationRepository()
    ): NotesUseCase {
        return NotesUseCase(documentRepository, notesConfigurationRepository)
    }

    internal fun provideChooseKmpNoteViewModel(
        notesUseCase: NotesUseCase = provideNotesUseCase(),
        notesConfig: NotesConfigurationRepository =
            notesConfigurationInjector.provideNotesConfigurationRepository()
    ): ChooseNoteKmpViewModel =
        ChooseNoteKmpViewModel(notesUseCase, notesConfig, authCoreInjection.provideAccountManager())

    fun provideChooseNoteViewModel(coroutineScope: CoroutineScope): ChooseNoteViewModel =
        provideChooseKmpNoteViewModel().apply {
            initCoroutine(coroutineScope)
        }
}

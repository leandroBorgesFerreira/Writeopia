package io.writeopia.note_menu.di

import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteKmpViewModel
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class NotesMenuKmpInjection(
    private val notesConfigurationInjector: NotesConfigurationInjector,
    private val authCoreInjection: AuthCoreInjection,
    private val repositoryInjection: RepositoryInjector,
    private val selectionState: StateFlow<Boolean>
) {

    private fun provideDocumentRepository(): DocumentRepository =
        repositoryInjection.provideDocumentRepository()

    private fun provideNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        configurationRepository: ConfigurationRepository =
            notesConfigurationInjector.provideNotesConfigurationRepository()
    ): NotesUseCase {
        return NotesUseCase(documentRepository, configurationRepository)
    }

    internal fun provideChooseKmpNoteViewModel(
        notesUseCase: NotesUseCase = provideNotesUseCase(),
        notesConfig: ConfigurationRepository =
            notesConfigurationInjector.provideNotesConfigurationRepository()
    ): ChooseNoteKmpViewModel =
        ChooseNoteKmpViewModel(
            notesUseCase,
            notesConfig,
            authCoreInjection.provideAccountManager(),
            selectionState
        )

    fun provideChooseNoteViewModel(coroutineScope: CoroutineScope): ChooseNoteViewModel =
        provideChooseKmpNoteViewModel().apply {
            initCoroutine(coroutineScope)
        }
}

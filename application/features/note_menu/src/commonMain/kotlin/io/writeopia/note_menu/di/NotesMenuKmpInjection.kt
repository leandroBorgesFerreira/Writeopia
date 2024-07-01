package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteKmpViewModel
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class NotesMenuKmpInjection(
    private val notesInjector: NotesInjector,
    private val authCoreInjection: AuthCoreInjection,
    private val repositoryInjection: RepositoryInjector,
    private val uiConfigurationInjector: UiConfigurationInjector,
    private val selectionState: StateFlow<Boolean>
) : NotesMenuInjection {

    private fun provideDocumentRepository(): DocumentRepository =
        repositoryInjection.provideDocumentRepository()

    private fun provideNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        configurationRepository: ConfigurationRepository =
            notesInjector.provideNotesConfigurationRepository(),
        folderRepository: FolderRepository = notesInjector.provideFoldersRepository()
    ): NotesUseCase {
        return NotesUseCase(documentRepository, configurationRepository, folderRepository)
    }

    internal fun provideChooseKmpNoteViewModel(
        notesNavigation: NotesNavigation = NotesNavigation.Root,
        notesUseCase: NotesUseCase = provideNotesUseCase(),
        notesConfig: ConfigurationRepository =
            notesInjector.provideNotesConfigurationRepository(),
        uiConfigurationRepo: UiConfigurationRepository =
            uiConfigurationInjector.provideUiConfigurationRepository()
    ): ChooseNoteKmpViewModel =
        ChooseNoteKmpViewModel(
            notesUseCase = notesUseCase,
            notesConfig = notesConfig,
            uiConfigurationRepo = uiConfigurationRepo,
            authManager = authCoreInjection.provideAccountManager(),
            selectionState = selectionState,
            notesNavigation = notesNavigation
        )

    @Composable
    override fun provideChooseNoteViewModel(
        coroutineScope: CoroutineScope?,
        notesNavigation: NotesNavigation
    ): ChooseNoteViewModel =
        remember {
            provideChooseKmpNoteViewModel(notesNavigation).apply {
                if (coroutineScope != null) {
                    initCoroutine(coroutineScope)
                }
            }
        }
}


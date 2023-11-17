package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.sdk.persistence.core.dao.DocumentRepository
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.usecase.NotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.persistence.data.daos.RoomNotesConfigurationDao
import io.writeopia.persistence.injection.RoomDaosInjection

class NotesMenuInjection(
    private val configurationDao: RoomNotesConfigurationDao,
    private val authManager: AuthManager,
    private val daosInjection: RoomDaosInjection
) {

    private fun provideDocumentRepository(): DocumentRepository = daosInjection.provideDocumentDao()

    private fun provideNotesConfigurationRepository(): NotesConfigurationRepository =
        NotesConfigurationRepository(configurationDao)

    private fun provideNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        notesConfigurationRepository: NotesConfigurationRepository =
            provideNotesConfigurationRepository()
    ): NotesUseCase {
        return NotesUseCase(documentRepository, notesConfigurationRepository)
    }

    @Composable
    internal fun provideChooseNoteViewModel(
        notesUseCase: NotesUseCase = provideNotesUseCase(),
        notesConfig: NotesConfigurationRepository = provideNotesConfigurationRepository()
    ): ChooseNoteViewModel {
        return viewModel(
            factory = ChooseNoteViewModelFactory(
                notesUseCase,
                notesConfig,
                authManager
            )
        )
    }
}

package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.usecase.RoomNotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.persistence.data.daos.NotesConfigurationDao
import io.writeopia.persistence.injection.RoomDaosInjection

class NotesMenuInjection(
    private val configurationDao: NotesConfigurationDao,
    private val authManager: AuthManager,
    private val daosInjection: RoomDaosInjection
) {

    private fun provideDocumentRepository(): DocumentDao = daosInjection.provideDocumentDao()

    private fun provideNotesConfigurationRepository(): RoomNotesConfigurationRepository =
        RoomNotesConfigurationRepository(configurationDao)

    private fun provideNotesUseCase(
        documentDao: DocumentDao = provideDocumentRepository(),
        notesConfigurationRepository: RoomNotesConfigurationRepository =
            provideNotesConfigurationRepository()
    ): NotesUseCase {
        return NotesUseCase(documentDao, notesConfigurationRepository)
    }

    @Composable
    internal fun provideChooseNoteViewModel(
        notesUseCase: NotesUseCase = provideNotesUseCase(),
        notesConfig: RoomNotesConfigurationRepository = provideNotesConfigurationRepository()
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

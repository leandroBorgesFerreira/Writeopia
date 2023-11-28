package io.writeopia.note_menu.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.sdk.persistence.core.dao.DocumentRepository
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteKmpViewModel
import io.writeopia.note_menu.data.repository.NotesConfigurationRepository
import io.writeopia.note_menu.data.repository.NotesConfigurationRoomRepository
import io.writeopia.note_menu.viewmodel.ChooseNoteAndroidViewModel
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.persistence.room.data.daos.NotesConfigurationRoomDao
import io.writeopia.persistence.room.injection.RoomDaosInjection

class NotesMenuInjection(
    private val configurationDao: NotesConfigurationRoomDao,
    private val authManager: AuthManager,
    private val daosInjection: RoomDaosInjection
) {

    private fun provideDocumentRepository(): DocumentRepository = daosInjection.provideDocumentDao()

    private fun provideNotesConfigurationRepository(): NotesConfigurationRepository =
        NotesConfigurationRoomRepository(configurationDao)

    private fun provideNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        notesConfigurationRepository: NotesConfigurationRepository = provideNotesConfigurationRepository()
    ): NotesUseCase {
        return NotesUseCase(documentRepository, notesConfigurationRepository)
    }

    private fun provideChooseKmpNoteViewModel(
        notesUseCase: NotesUseCase = provideNotesUseCase(),
        notesConfig: NotesConfigurationRepository = provideNotesConfigurationRepository()
    ): ChooseNoteKmpViewModel = ChooseNoteKmpViewModel(notesUseCase, notesConfig, authManager)

    @Composable
    internal fun provideChooseNoteViewModel(
        chooseNoteKmpViewModel: ChooseNoteKmpViewModel = provideChooseKmpNoteViewModel()
    ): ChooseNoteViewModel = viewModel { ChooseNoteAndroidViewModel(chooseNoteKmpViewModel) }
}

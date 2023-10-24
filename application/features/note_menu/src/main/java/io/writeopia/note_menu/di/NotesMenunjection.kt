package io.writeopia.note_menu.di

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.usecase.NotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.persistence.injection.RepositoriesInjection

class NotesMenuInjection(
    private val sharedPreferences: SharedPreferences,
    private val authManager: AuthManager,
    private val repositoriesInjection: RepositoriesInjection
) {

    private fun provideDocumentRepository(): DocumentRepository = repositoriesInjection.provideDocumentRepository()

    private fun provideNotesConfigurationRepository(): NotesConfigurationRepository =
        NotesConfigurationRepository(sharedPreferences)

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

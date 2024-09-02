package io.writeopia.global.shell.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.global.shell.viewmodel.SideMenuKmpViewModel
import io.writeopia.global.shell.viewmodel.SideMenuViewModel
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.note_menu.data.usecase.NoteNavigationUseCase
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.di.NotesInjector
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.note_menu.viewmodel.FolderStateController
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class SideMenuKmpInjector(
    private val notesInjector: NotesInjector,
    private val authCoreInjection: AuthCoreInjection,
    private val repositoryInjection: RepositoryInjector,
    private val uiConfigurationInjector: UiConfigurationInjector,
    private val selectionState: StateFlow<Boolean>
) : SideMenuInjector {
    private fun provideDocumentRepository(): DocumentRepository =
        repositoryInjection.provideDocumentRepository()

    private fun provideNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        configurationRepository: ConfigurationRepository =
            notesInjector.provideNotesConfigurationRepository(),
        folderRepository: FolderRepository = notesInjector.provideFoldersRepository()
    ): NotesUseCase {
        return NotesUseCase.singleton(documentRepository, configurationRepository, folderRepository)
    }

    @Composable
    override fun provideSideMenuViewModel(coroutineScope: CoroutineScope?): SideMenuViewModel =
        remember {
            SideMenuKmpViewModel(
                notesUseCase = provideNotesUseCase(),
                uiConfigurationRepo = uiConfigurationInjector.provideUiConfigurationRepository(),
                authManager = authCoreInjection.provideAccountManager(),
                notesNavigationUseCase = NoteNavigationUseCase.singleton(),
            ).apply {
                if (coroutineScope != null) {
                    this.initCoroutine(coroutineScope)
                }
            }
        }
}

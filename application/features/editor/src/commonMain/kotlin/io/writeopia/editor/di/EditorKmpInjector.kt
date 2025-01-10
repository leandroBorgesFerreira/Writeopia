package io.writeopia.editor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.editor.features.editor.viewmodel.NoteEditorKmpViewModel
import io.writeopia.editor.features.editor.viewmodel.NoteEditorViewModel
import io.writeopia.editor.features.presentation.viewmodel.PresentationKmpViewModel
import io.writeopia.editor.features.presentation.viewmodel.PresentationViewModel
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.sharededition.SharedEditionManager
import io.writeopia.ui.keyboard.KeyboardEvent
import io.writeopia.ui.manager.WriteopiaStateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditorKmpInjector private constructor(
    private val authCoreInjection: AuthCoreInjection,
    private val repositoryInjection: RepositoryInjector,
    private val connectionInjection: ConnectionInjector,
    private val selectionState: StateFlow<Boolean>,
    private val keyboardEventFlow: Flow<KeyboardEvent>,
    private val uiConfigurationRepository: UiConfigurationRepository
) : TextEditorInjector {

    private fun provideDocumentRepository(): DocumentRepository =
        repositoryInjection.provideDocumentRepository()

    private fun provideWriteopiaManager(): WriteopiaManager = WriteopiaManager()

    private fun provideWriteopiaStateManager(
        authManager: AuthManager = authCoreInjection.provideAccountManager(),
        writeopiaManager: WriteopiaManager = provideWriteopiaManager()
    ) = WriteopiaStateManager.create(
        userId = { authManager.getUser().id },
        dispatcher = Dispatchers.Default,
        writeopiaManager = writeopiaManager,
        selectionState = selectionState,
        keyboardEventFlow = keyboardEventFlow
    )

    fun provideNoteEditorViewModel(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        writeopiaManager: WriteopiaStateManager = provideWriteopiaStateManager(),
        sharedEditionManager: SharedEditionManager = connectionInjection.liveEditionManager(),
        parentFolder: String,
    ): NoteEditorKmpViewModel =
        NoteEditorKmpViewModel(
            writeopiaManager,
            documentRepository,
            sharedEditionManager = sharedEditionManager,
            parentFolderId = parentFolder,
            uiConfigurationRepository = uiConfigurationRepository
        )

    @Composable
    override fun providePresentationViewModel(): PresentationViewModel = viewModel {
        PresentationKmpViewModel(documentRepository = provideDocumentRepository())
    }

    @Composable
    override fun provideNoteDetailsViewModel(parentFolder: String): NoteEditorViewModel =
        viewModel {
            provideNoteEditorViewModel(parentFolder = parentFolder)
        }

    companion object {
        fun mobile(
            authCoreInjection: AuthCoreInjection,
            daosInjection: RepositoryInjector,
            connectionInjector: ConnectionInjector,
            uiConfigurationRepository: UiConfigurationRepository
        ) = EditorKmpInjector(
            authCoreInjection,
            daosInjection,
            connectionInjector,
            MutableStateFlow(false),
            MutableStateFlow(KeyboardEvent.IDLE),
            uiConfigurationRepository
        )

        fun desktop(
            authCoreInjection: AuthCoreInjection,
            repositoryInjection: RepositoryInjector,
            connectionInjection: ConnectionInjector,
            selectionState: StateFlow<Boolean>,
            keyboardEventFlow: Flow<KeyboardEvent>,
            uiConfigurationRepository: UiConfigurationRepository
        ) = EditorKmpInjector(
            authCoreInjection,
            repositoryInjection,
            connectionInjection,
            selectionState,
            keyboardEventFlow,
            uiConfigurationRepository
        )
    }
}

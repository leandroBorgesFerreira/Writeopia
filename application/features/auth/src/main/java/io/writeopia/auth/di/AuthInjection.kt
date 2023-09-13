package io.writeopia.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.network.injector.ApiInjector
import io.writeopia.sdk.network.notes.NotesApi
import io.writeopia.sdk.persistence.database.WriteopiaDatabase
import io.writeopia.sdk.persistence.repository.DocumentRepositoryImpl
import io.writeopia.auth.core.AuthManager
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.auth.intronotes.IntroNotesUseCase
import io.writeopia.auth.login.LoginViewModel
import io.writeopia.auth.menu.AuthMenuViewModel
import io.writeopia.auth.register.RegisterViewModel

class AuthInjection(
    private val authCoreInjection: AuthCoreInjection,
    private val database: WriteopiaDatabase,
    private val apiInjector: ApiInjector,
) {

    private fun provideDocumentRepository(): DocumentRepository =
        DocumentRepositoryImpl(
            database.documentDao(),
            database.storyUnitDao()
        )

    private fun provideIntroNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        notesApi: NotesApi = apiInjector.notesApi()
    ): IntroNotesUseCase =
        IntroNotesUseCase(
            documentRepository = documentRepository,
            notesApi = notesApi
        )

    @Composable
    internal fun provideRegisterViewModel(
        introNotesUseCase: IntroNotesUseCase = provideIntroNotesUseCase(),
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ): RegisterViewModel = viewModel { RegisterViewModel(introNotesUseCase, authManager) }

    @Composable
    internal fun provideLoginViewModel(
        introNotesUseCase: IntroNotesUseCase = provideIntroNotesUseCase(),
        authManager: AuthManager = authCoreInjection.provideAccountManager()
    ): LoginViewModel = viewModel { LoginViewModel(introNotesUseCase, authManager) }

    @Composable
    internal fun provideAuthMenuViewModel(
        authManager: AuthManager = authCoreInjection.provideAccountManager(),
        authRepository: AuthRepository = authCoreInjection.provideAuthRepository(),
    ): AuthMenuViewModel = viewModel { AuthMenuViewModel(authManager, authRepository) }

}

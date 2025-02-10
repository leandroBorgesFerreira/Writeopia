package io.writeopia.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.auth.intronotes.IntroNotesUseCase
import io.writeopia.auth.login.LoginViewModel
import io.writeopia.auth.menu.AuthMenuViewModel
import io.writeopia.auth.register.RegisterViewModel
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.sdk.network.notes.NotesApi
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.repository.DocumentRepository

class AuthInjection(
    private val authCoreInjection: AuthCoreInjection,
    private val apiClientInjector: ConnectionInjector,
    private val daosInjection: RepositoryInjector
) {

    private fun provideDocumentRepository(): DocumentRepository = daosInjection.provideDocumentRepository()

    private fun provideIntroNotesUseCase(
        documentRepository: DocumentRepository = provideDocumentRepository(),
        notesApi: NotesApi = apiClientInjector.notesApi()
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

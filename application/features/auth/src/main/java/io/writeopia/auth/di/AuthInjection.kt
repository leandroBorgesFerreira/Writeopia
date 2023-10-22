package io.writeopia.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.auth.core.AuthManager
import io.writeopia.auth.core.di.AuthCoreInjection
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.auth.intronotes.IntroNotesUseCase
import io.writeopia.auth.login.LoginViewModel
import io.writeopia.auth.menu.AuthMenuViewModel
import io.writeopia.auth.register.RegisterViewModel
import io.writeopia.persistence.injection.RepositoriesInjection
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.network.injector.ApiInjector
import io.writeopia.sdk.network.notes.NotesApi

class AuthInjection(
    private val authCoreInjection: AuthCoreInjection,
    private val apiInjector: ApiInjector,
    private val repositoriesInjection: RepositoriesInjection
) {

    private fun provideDocumentRepository(): DocumentRepository = repositoriesInjection.provideDocumentRepository()

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

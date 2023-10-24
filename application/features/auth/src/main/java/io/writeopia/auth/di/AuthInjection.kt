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
import io.writeopia.persistence.injection.DaoInjection
import io.writeopia.sdk.manager.DocumentDao
import io.writeopia.sdk.network.injector.ApiInjector
import io.writeopia.sdk.network.notes.NotesApi

class AuthInjection(
    private val authCoreInjection: AuthCoreInjection,
    private val apiInjector: ApiInjector,
    private val daoInjection: DaoInjection
) {

    private fun provideDocumentRepository(): DocumentDao = daoInjection.provideDocumentDao()

    private fun provideIntroNotesUseCase(
        documentDao: DocumentDao = provideDocumentRepository(),
        notesApi: NotesApi = apiInjector.notesApi()
    ): IntroNotesUseCase =
        IntroNotesUseCase(
            documentDao = documentDao,
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

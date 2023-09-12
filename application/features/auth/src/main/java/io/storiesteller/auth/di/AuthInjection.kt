package io.storiesteller.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.storiesteller.sdk.manager.DocumentRepository
import io.storiesteller.sdk.network.injector.ApiInjector
import io.storiesteller.sdk.network.notes.NotesApi
import io.storiesteller.sdk.persistence.database.StoriesTellerDatabase
import io.storiesteller.sdk.persistence.repository.DocumentRepositoryImpl
import io.storiesteller.auth.core.AuthManager
import io.storiesteller.auth.core.di.AuthCoreInjection
import io.storiesteller.auth.core.repository.AuthRepository
import io.storiesteller.auth.intronotes.IntroNotesUseCase
import io.storiesteller.auth.login.LoginViewModel
import io.storiesteller.auth.menu.AuthMenuViewModel
import io.storiesteller.auth.register.RegisterViewModel

class AuthInjection(
    private val authCoreInjection: AuthCoreInjection,
    private val database: StoriesTellerDatabase,
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

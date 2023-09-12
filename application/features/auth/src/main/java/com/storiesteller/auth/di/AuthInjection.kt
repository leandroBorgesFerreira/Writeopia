package com.storiesteller.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.storiesteller.sdk.manager.DocumentRepository
import com.storiesteller.sdk.network.injector.ApiInjector
import com.storiesteller.sdk.network.notes.NotesApi
import com.storiesteller.sdk.persistence.database.StoryTellerDatabase
import com.storiesteller.sdk.persistence.repository.DocumentRepositoryImpl
import com.storiesteller.auth.core.AuthManager
import com.storiesteller.auth.core.di.AuthCoreInjection
import com.storiesteller.auth.core.repository.AuthRepository
import com.storiesteller.auth.intronotes.IntroNotesUseCase
import com.storiesteller.auth.login.LoginViewModel
import com.storiesteller.auth.menu.AuthMenuViewModel
import com.storiesteller.auth.register.RegisterViewModel

class AuthInjection(
    private val authCoreInjection: AuthCoreInjection,
    private val database: StoryTellerDatabase,
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

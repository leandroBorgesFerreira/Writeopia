package com.github.leandroborgesferreira.storytellerapp.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.storiesteller.sdk.manager.DocumentRepository
import com.storiesteller.sdk.network.injector.ApiInjector
import com.storiesteller.sdk.network.notes.NotesApi
import com.storiesteller.sdk.persistence.database.StoryTellerDatabase
import com.storiesteller.sdk.persistence.repository.DocumentRepositoryImpl
import com.github.leandroborgesferreira.storytellerapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.auth.core.di.AuthCoreInjection
import com.github.leandroborgesferreira.storytellerapp.auth.core.repository.AuthRepository
import com.github.leandroborgesferreira.storytellerapp.auth.intronotes.IntroNotesUseCase
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.menu.AuthMenuViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterViewModel

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

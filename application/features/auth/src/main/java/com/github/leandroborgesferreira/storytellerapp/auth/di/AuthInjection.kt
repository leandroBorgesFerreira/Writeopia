package com.github.leandroborgesferreira.storytellerapp.auth.di

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.network.injector.ApiInjector
import com.github.leandroborgesferreira.storyteller.network.notes.NotesApi
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl
import com.github.leandroborgesferreira.storytellerapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.auth.intronotes.IntroNotesUseCase
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.menu.AuthMenuViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterViewModel

class AuthInjection(
    private val sharedPreferences: SharedPreferences,
    private val database: StoryTellerDatabase,
    private val apiInjector: ApiInjector
) {

    fun provideAccountManager(): AuthManager = AuthManager(sharedPreferences)

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
        authManager: AuthManager = provideAccountManager()
    ): RegisterViewModel = viewModel { RegisterViewModel(introNotesUseCase, authManager) }

    @Composable
    internal fun provideLoginViewModel(
        introNotesUseCase: IntroNotesUseCase = provideIntroNotesUseCase(),
        authManager: AuthManager = provideAccountManager()
    ): LoginViewModel = viewModel { LoginViewModel(introNotesUseCase, authManager) }

    @Composable
    internal fun provideAuthMenuViewModel(
        authManager: AuthManager = provideAccountManager()
    ): AuthMenuViewModel = viewModel { AuthMenuViewModel(authManager) }

}

package com.github.leandroborgesferreira.storytellerapp.auth.di

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.network.injector.ApiInjector
import com.github.leandroborgesferreira.storyteller.network.notes.NotesApi
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl
import com.github.leandroborgesferreira.storytellerapp.auth.core.AccountManager
import com.github.leandroborgesferreira.storytellerapp.auth.intronotes.IntroNotesUseCase
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterViewModel

class AuthInjection(
    private val sharedPreferences: SharedPreferences,
    private val database: StoryTellerDatabase,
    private val apiInjector: ApiInjector
) {

    fun provideAccountManager(): AccountManager = AccountManager(sharedPreferences)

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
        accountManager: AccountManager = provideAccountManager()
    ): RegisterViewModel = viewModel { RegisterViewModel(introNotesUseCase, accountManager) }

    @Composable
    internal fun provideLoginViewModel(
        introNotesUseCase: IntroNotesUseCase = provideIntroNotesUseCase(),
        accountManager: AccountManager = provideAccountManager()
    ): LoginViewModel = viewModel { LoginViewModel(introNotesUseCase, accountManager) }
}

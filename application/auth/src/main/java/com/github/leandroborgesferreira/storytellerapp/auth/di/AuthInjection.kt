package com.github.leandroborgesferreira.storytellerapp.auth.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.network.injector.ApiInjector
import com.github.leandroborgesferreira.storyteller.network.notes.NotesApi
import com.github.leandroborgesferreira.storyteller.persistence.database.StoryTellerDatabase
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl
import com.github.leandroborgesferreira.storytellerapp.auth.intronotes.IntroNotesUseCase
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterViewModel

class AuthInjection(
    private val database: StoryTellerDatabase,
    private val apiInjector: ApiInjector
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
    fun provideRegisterViewModel(): RegisterViewModel =
        viewModel(initializer = { RegisterViewModel(provideIntroNotesUseCase()) })

    @Composable
    fun provideLoginViewModel(): LoginViewModel =
        viewModel(initializer = { LoginViewModel(provideIntroNotesUseCase()) })
}

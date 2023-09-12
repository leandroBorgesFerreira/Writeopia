package com.github.leandroborgesferreira.storytellerapp.note_menu.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.leandroborgesferreira.storytellerapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesConfigurationRepository
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesUseCase
import com.github.leandroborgesferreira.storytellerapp.note_menu.viewmodel.ChooseNoteViewModel

internal class ChooseNoteViewModelFactory(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: NotesConfigurationRepository,
    private val authManager: AuthManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChooseNoteViewModel::class.java)) {
            return ChooseNoteViewModel(notesUseCase, notesConfig, authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package io.writeopia.note_menu.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.repository.NotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel

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
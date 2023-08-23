package com.github.leandroborgesferreira.storytellerapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.leandroborgesferreira.storytellerapp.editor.NoteEditorViewModel
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesConfigurationRepository
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesUseCase
import com.github.leandroborgesferreira.storytellerapp.note_menu.viewmodel.ChooseNoteViewModel
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl

class ChooseNoteViewModelFactory(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: NotesConfigurationRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChooseNoteViewModel::class.java)) {
            return ChooseNoteViewModel(notesUseCase, notesConfig) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
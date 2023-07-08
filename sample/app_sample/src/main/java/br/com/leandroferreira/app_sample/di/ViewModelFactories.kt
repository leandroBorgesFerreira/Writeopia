package br.com.leandroferreira.app_sample.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.leandroferreira.editor.NoteDetailsViewModel
import br.com.leandroferreira.note_menu.data.usecase.NotesConfigurationRepository
import br.com.leandroferreira.note_menu.data.usecase.NotesUseCase
import br.com.leandroferreira.note_menu.viewmodel.ChooseNoteViewModel
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl

class NoteDetailsViewModelFactory(
    private val storyTellerManager: StoryTellerManager,
    private val documentRepository: DocumentRepositoryImpl
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailsViewModel::class.java)) {
            return NoteDetailsViewModel(storyTellerManager, documentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


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
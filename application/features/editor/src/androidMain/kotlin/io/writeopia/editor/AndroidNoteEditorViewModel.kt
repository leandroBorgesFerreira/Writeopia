package io.writeopia.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.writeopia.editor.viewmodel.NoteEditorKmpViewModel
import io.writeopia.editor.viewmodel.NoteEditorViewModel

internal class AndroidNoteEditorViewModel(
    private val noteEditorKmpViewModel: NoteEditorKmpViewModel
) : ViewModel(), NoteEditorViewModel by noteEditorKmpViewModel {

    init {
        noteEditorKmpViewModel.initCoroutine(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        onViewModelCleared()
    }
}

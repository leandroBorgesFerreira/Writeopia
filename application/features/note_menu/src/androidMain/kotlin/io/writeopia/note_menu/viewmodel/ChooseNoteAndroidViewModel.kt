package io.writeopia.note_menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

internal class ChooseNoteAndroidViewModel(
    private val chooseNoteKmpViewModel: ChooseNoteKmpViewModel
) : ViewModel(), ChooseNoteViewModel by chooseNoteKmpViewModel {

    init {
        chooseNoteKmpViewModel.initCoroutine(viewModelScope)
    }
}

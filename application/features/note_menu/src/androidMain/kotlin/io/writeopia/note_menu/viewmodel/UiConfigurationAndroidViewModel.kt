package io.writeopia.note_menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.writeopia.viewmodel.UiConfigurationKmpViewModel
import io.writeopia.viewmodel.UiConfigurationViewModel

class UiConfigurationAndroidViewModel(
    private val uiConfigurationKmpViewModel: UiConfigurationKmpViewModel
) : UiConfigurationViewModel by uiConfigurationKmpViewModel, ViewModel() {

    init {
        uiConfigurationKmpViewModel.initCoroutine(viewModelScope)
    }
}

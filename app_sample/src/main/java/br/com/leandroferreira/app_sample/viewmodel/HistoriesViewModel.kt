package br.com.leandroferreira.app_sample.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoriesViewModel {

    private val _editModeState = MutableStateFlow(false)
    val editModeState: StateFlow<Boolean> = _editModeState

    fun toggleEdit() {
        _editModeState.value = !_editModeState.value
    }
}

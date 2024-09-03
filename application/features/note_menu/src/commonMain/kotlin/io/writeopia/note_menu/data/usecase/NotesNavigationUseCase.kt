package io.writeopia.note_menu.data.usecase

import io.writeopia.note_menu.data.model.NotesNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotesNavigationUseCase private constructor() {
    private val _navigationState = MutableStateFlow<NotesNavigation>(NotesNavigation.Root)
    val navigationState = _navigationState.asStateFlow()

    fun setNoteNavigation(navigation: NotesNavigation) {
        _navigationState.value = navigation
    }

    companion object {
        var instance: NotesNavigationUseCase? = null

        fun singleton(): NotesNavigationUseCase =
            instance ?: NotesNavigationUseCase().also {
                instance = it
            }
    }
}

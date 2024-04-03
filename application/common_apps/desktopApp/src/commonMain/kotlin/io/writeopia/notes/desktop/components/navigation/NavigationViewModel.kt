package io.writeopia.notes.desktop.components.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel {

    private val _currentState = MutableStateFlow<NavigationPage>(NavigationPage.NoteMenu)
    val navigationPage = _currentState.asStateFlow()

    fun navigateTo(navigationPage: NavigationPage) {
        _currentState.value = navigationPage
    }
}
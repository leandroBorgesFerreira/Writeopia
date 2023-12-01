package io.writeopia.notes.desktop.components.navigation

enum class NavigationState {
    NOTE_MENU, EDITOR
}

sealed interface NavigationPage {
    data object NoteMenu : NavigationPage
    data class Editor(val noteId: String? = null, val title: String? = null) : NavigationPage
}
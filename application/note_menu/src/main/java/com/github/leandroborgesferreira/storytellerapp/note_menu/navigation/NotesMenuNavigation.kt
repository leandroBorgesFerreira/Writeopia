package com.github.leandroborgesferreira.storytellerapp.note_menu.navigation

import com.github.leandroborgesferreira.storytellerapp.note_menu.di.NotesMenuInjection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.leandroborgesferreira.storytellerapp.note_menu.ui.screen.menu.ChooseNoteScreen
import com.github.leandroborgesferreira.storytellerapp.utils_module.Destinations

fun NavGraphBuilder.notesMenuNavigation(
    notesMenuInjection: NotesMenuInjection,
    navigateToNote: (String, String) -> Unit,
    navigateToNewNote: () -> Unit,
    navigateToAuthMenu: () -> Unit,
) {
    composable(Destinations.CHOOSE_NOTE.id) {
        val chooseNoteViewModel = notesMenuInjection.provideChooseNoteViewModel()

        ChooseNoteScreen(
            chooseNoteViewModel = chooseNoteViewModel,
            navigateToNote = navigateToNote,
            newNote = navigateToNewNote,
            onLogout = navigateToAuthMenu
        )
    }
}

package com.github.leandroborgesferreira.storytellerapp.note_menu.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
    navigateToAccount: () -> Unit,
) {
    composable(
        Destinations.CHOOSE_NOTE.id,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { intSize -> -intSize })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { intSize -> -intSize })
        }
    ) {
        val chooseNoteViewModel = notesMenuInjection.provideChooseNoteViewModel()

        ChooseNoteScreen(
            chooseNoteViewModel = chooseNoteViewModel,
            navigateToNote = navigateToNote,
            navigateToAccount = navigateToAccount,
            newNote = navigateToNewNote,
            onLogout = navigateToAuthMenu,
        )
    }
}

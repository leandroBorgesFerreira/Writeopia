package io.writeopia.note_menu.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.writeopia.note_menu.di.NotesMenuInjection
import io.writeopia.note_menu.ui.screen.menu.NotesMenuScreen
import io.writeopia.utils_module.Destinations

fun NavGraphBuilder.notesMenuNavigation(
    notesMenuInjection: NotesMenuInjection,
    navigateToNote: (String, String) -> Unit,
    navigateToNewNote: () -> Unit,
    navigateToAccount: () -> Unit,
) {

    composable(
        Destinations.CHOOSE_NOTE.id,
        enterTransition = {
            val isFirstScreen = this.initialState.destination
                .route
                ?.startsWith(Destinations.AUTH_MENU.id)

            if (isFirstScreen == true) {
                fadeIn()
            } else {
                slideInHorizontally(initialOffsetX = { intSize -> -intSize })
            }
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { intSize -> -intSize })
        }
    ) {
        val chooseNoteViewModel =
            notesMenuInjection.provideChooseNoteViewModel(coroutineScope = rememberCoroutineScope())

        NotesMenuScreen(
            chooseNoteViewModel = chooseNoteViewModel,
            onNewNoteClick = navigateToNewNote,
            onNoteClick = navigateToNote,
            onAccountClick = navigateToAccount,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

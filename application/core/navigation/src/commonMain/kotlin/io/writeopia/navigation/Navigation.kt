package io.writeopia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.writeopia.navigation.notes.navigateToAccount
import io.writeopia.navigation.notes.navigateToNewNote
import io.writeopia.navigation.notes.navigateToNote
import io.writeopia.note_menu.di.NotesMenuInjection
import io.writeopia.note_menu.navigation.notesMenuNavigation
import io.writeopia.utils_module.Destinations

@Composable
fun Navigation(
    startDestination: String = Destinations.AUTH_MENU_INNER_NAVIGATION.id,
    notesMenuInjection: NotesMenuInjection
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        notesMenuNavigation(
            notesMenuInjection = notesMenuInjection,
            navigateToNote = navController::navigateToNote,
            navigateToAccount = navController::navigateToAccount,
            navigateToNewNote = navController::navigateToNewNote
        )
    }
}



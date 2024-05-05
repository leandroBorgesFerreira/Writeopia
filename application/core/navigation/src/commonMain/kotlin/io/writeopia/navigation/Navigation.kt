package io.writeopia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    startDestination: String = Destinations.AUTH_MENU_INNER_NAVIGATION.id
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
//        notesMenuNavigation(
//            notesMenuInjection = notesMenuInjection,
//            navigateToNote = navController::navigateToNote,
//            navigateToAccount = navController::navigateToAccount,
//            navigateToNewNote = navController::navigateToNewNote
//        )
    }
}



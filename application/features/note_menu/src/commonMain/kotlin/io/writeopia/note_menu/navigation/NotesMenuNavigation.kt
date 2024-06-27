package io.writeopia.note_menu.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.model.NotesNavigationType
import io.writeopia.note_menu.di.NotesMenuInjection
import io.writeopia.note_menu.ui.screen.menu.NotesMenuScreen
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.utils_module.Destinations
import kotlinx.coroutines.CoroutineScope

private const val NAVIGATION_TYPE = "type"
private const val NAVIGATION_PATH = "path"

fun NavGraphBuilder.notesMenuNavigation(
    notesMenuInjection: NotesMenuInjection,
    navigationController: NavController,
    coroutineScope: CoroutineScope?,
    selectColorTheme: (ColorThemeOption) -> Unit,
    navigateToNote: (String, String) -> Unit,
    navigateToNewNote: () -> Unit,
    navigateToAccount: () -> Unit,
) {

    composable(
        route = "${Destinations.CHOOSE_NOTE.id}/{$NAVIGATION_TYPE}/{$NAVIGATION_PATH}",
        arguments = listOf(
            navArgument(NAVIGATION_TYPE) {
                type = NavType.StringType
                defaultValue = NotesNavigationType.ROOT.type
            },
            navArgument(NAVIGATION_PATH) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
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
    ) { backStackEntry ->
        val notesNavigation = backStackEntry.arguments?.getString(NAVIGATION_TYPE)?.let { type ->
            NotesNavigation.fromType(NotesNavigationType.fromType(type), "")
        } ?: NotesNavigation.Root

        val chooseNoteViewModel: ChooseNoteViewModel =
            notesMenuInjection.provideChooseNoteViewModel(
                coroutineScope = coroutineScope,
                notesNavigation = notesNavigation
            )

        NotesMenuScreen(
            chooseNoteViewModel = chooseNoteViewModel,
            navigationController = navigationController,
            onNewNoteClick = navigateToNewNote,
            onNoteClick = navigateToNote,
            onAccountClick = navigateToAccount,
            selectColorTheme = selectColorTheme,
            navigateToNotes = { navigation ->
                navigationController.navigate(
                    "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/path",
                )
            },
            addFolder = chooseNoteViewModel::addFolder,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

package io.writeopia.note_menu.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.writeopia.common.utils.Destinations
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.data.model.NotesNavigation
import io.writeopia.note_menu.data.model.NotesNavigationType
import io.writeopia.note_menu.di.NotesMenuInjection
import io.writeopia.note_menu.ui.screen.menu.NotesMenuScreen
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import kotlinx.coroutines.CoroutineScope

const val NAVIGATION_TYPE = "type"
const val NAVIGATION_PATH = "path"

object NoteMenuDestiny {
    fun noteMenu() = "${Destinations.CHOOSE_NOTE.id}/{$NAVIGATION_TYPE}/{$NAVIGATION_PATH}"
}

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
        route = NoteMenuDestiny.noteMenu(),
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
    ) { backStackEntry ->
        val navigationType = backStackEntry.arguments?.getString(NAVIGATION_TYPE)
        val navigationPath = backStackEntry.arguments?.getString(NAVIGATION_PATH)
        val notesNavigation = if (navigationType != null && navigationPath != null) {
            NotesNavigation.fromType(
                NotesNavigationType.fromType(navigationType),
                navigationPath
            )
        } else {
            NotesNavigation.Root
        }

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
                when (navigation) {
                    is NotesNavigation.Folder -> navigationController.navigate(
                        "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/${navigation.id}",
                    )

                    NotesNavigation.Favorites, NotesNavigation.Root -> navigationController.navigate(
                        "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/path",
                    )
                }
            },
            addFolder = chooseNoteViewModel::addFolder,
            editFolder = chooseNoteViewModel::editFolder,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

fun NavHostController.navigateToNotes(navigation: NotesNavigation) {
    when (navigation) {
        is NotesNavigation.Folder -> {
            navigate(
                "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/${navigation.id}",
            )
        }

        NotesNavigation.Favorites, NotesNavigation.Root -> navigate(
            "${Destinations.CHOOSE_NOTE.id}/${navigation.navigationType.type}/path",
        )
    }
}

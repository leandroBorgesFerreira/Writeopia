package com.github.leandroborgesferreira.storytellerapp.editor.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.leandroborgesferreira.storytellerapp.editor.NoteEditorScreen
import com.github.leandroborgesferreira.storytellerapp.editor.di.EditorInjector
import com.github.leandroborgesferreira.storytellerapp.utils_module.Destinations

fun NavGraphBuilder.editorNavigation(
    editorInjector: EditorInjector,
    navigateToNoteMenu: () -> Unit
) {
    composable(
        route = "${Destinations.EDITOR.id}/{noteId}/{noteTitle}",
        arguments = listOf(navArgument("noteId") { type = NavType.StringType }),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { intSize -> intSize }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { intSize -> intSize }
            )
        }
    ) { backStackEntry ->
        val noteId = backStackEntry.arguments?.getString("noteId")
        val noteTitle = backStackEntry.arguments?.getString("noteTitle")

        if (noteId != null && noteTitle != null) {
            val noteDetailsViewModel = editorInjector.provideNoteDetailsViewModel()

            NoteEditorScreen(
                noteId.takeIf { it != "null" },
                noteTitle.takeIf { it != "null" },
                noteDetailsViewModel,
                navigateBack = navigateToNoteMenu
            )
        } else {
            throw IllegalArgumentException("The arguments for this route are wrong!")
        }
    }

    composable(route = Destinations.EDITOR.id) {
        NoteEditorScreen(
            documentId = null,
            title = null,
            noteEditorViewModel = editorInjector.provideNoteDetailsViewModel(),
            navigateBack = navigateToNoteMenu
        )
    }
}
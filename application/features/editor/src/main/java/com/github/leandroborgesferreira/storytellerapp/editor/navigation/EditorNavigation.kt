package com.storiesteller.sdkapp.editor.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.storiesteller.sdkapp.editor.NoteEditorScreen
import com.storiesteller.sdkapp.editor.di.EditorInjector
import com.storiesteller.sdkapp.utils_module.Destinations

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
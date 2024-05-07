package io.writeopia.editor.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.writeopia.editor.di.TextEditorInjector
import io.writeopia.editor.ui.screen.TextEditorScreen
import io.writeopia.utils_module.Destinations

fun NavGraphBuilder.editorNavigation(
    editorInjector: TextEditorInjector,
    navigateToNoteMenu: () -> Unit,
    isUndoKeyEvent: (KeyEvent) -> Boolean
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

            TextEditorScreen(
                noteId.takeIf { it != "null" },
                noteTitle.takeIf { it != "null" },
                noteDetailsViewModel,
                isUndoKeyEvent = isUndoKeyEvent,
                navigateBack = navigateToNoteMenu
            )
        } else {
            throw IllegalArgumentException("The arguments for this route are wrong!")
        }
    }

    composable(route = Destinations.EDITOR.id) {
        val notesDetailsViewModel = editorInjector.provideNoteDetailsViewModel()

        TextEditorScreen(
            documentId = null,
            title = null,
            noteEditorViewModel = notesDetailsViewModel,
            isUndoKeyEvent = isUndoKeyEvent,
            navigateBack = navigateToNoteMenu,
            modifier = Modifier
        )
    }
}

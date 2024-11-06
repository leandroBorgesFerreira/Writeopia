package io.writeopia.editor.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.writeopia.common.utils.Destinations
import io.writeopia.editor.di.TextEditorInjector
import io.writeopia.editor.features.editor.ui.screen.TextEditorScreen
import io.writeopia.editor.features.editor.viewmodel.NoteEditorViewModel
import io.writeopia.editor.features.presentation.ui.PresentationScreen

fun NavGraphBuilder.editorNavigation(
    navigateBack: () -> Unit = {},
    editorInjector: TextEditorInjector,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    navigateToPresentation: (String) -> Unit
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
        val parentFolderId = backStackEntry.arguments?.getString("parentFolderId")

        if (noteId != null && noteTitle != null) {
            val noteDetailsViewModel =
                editorInjector.provideNoteDetailsViewModel(parentFolderId ?: "root")

            TextEditorScreen(
                noteId.takeIf { it != "null" },
                noteTitle.takeIf { it != "null" },
                noteDetailsViewModel,
                isUndoKeyEvent = isUndoKeyEvent,
                playPresentation = {
                    navigateToPresentation(noteId)
                },
                navigateBack = navigateBack,
            )
        } else {
            throw IllegalArgumentException("The arguments for this route are wrong!")
        }
    }

    composable(route = "${Destinations.EDITOR.id}/{parentFolderId}") { backStackEntry ->
        val parentFolderId = backStackEntry.arguments?.getString("parentFolderId")
        val notesDetailsViewModel: NoteEditorViewModel =
            editorInjector.provideNoteDetailsViewModel(parentFolderId ?: "root")

        TextEditorScreen(
            documentId = null,
            title = null,
            noteEditorViewModel = notesDetailsViewModel,
            isUndoKeyEvent = isUndoKeyEvent,
            navigateBack = navigateBack,
            playPresentation = {
                notesDetailsViewModel.writeopiaManager
                    .currentDocument
                    .value
                    ?.id
                    ?.let { documentId -> navigateToPresentation(documentId) }
            },
            modifier = Modifier,
        )
    }

    composable(route = "${Destinations.PRESENTATION.id}/{documentId}") { backStackEntry ->
        val documentId = backStackEntry.arguments?.getString("documentId")
        val viewModel = editorInjector.providePresentationViewModel(rememberCoroutineScope())

        if (documentId != null) {
            viewModel.loadDocument(documentId)
        }

        PresentationScreen(viewModel)
    }
}

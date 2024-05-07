package io.writeopia.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.KeyEvent
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.writeopia.account.di.AccountMenuInjector
import io.writeopia.account.navigation.accountMenuNavigation
import io.writeopia.editor.di.TextEditorInjector
import io.writeopia.editor.navigation.editorNavigation
import io.writeopia.navigation.notes.navigateToAccount
import io.writeopia.navigation.notes.navigateToNewNote
import io.writeopia.navigation.notes.navigateToNote
import io.writeopia.navigation.notes.navigateToNoteMenu
import io.writeopia.note_menu.di.NotesMenuInjection
import io.writeopia.note_menu.navigation.notesMenuNavigation
import io.writeopia.utils_module.Destinations

@Composable
fun Navigation(
    startDestination: String = Destinations.AUTH_MENU_INNER_NAVIGATION.id,
    navController: NavHostController = rememberNavController(),
    notesMenuInjection: NotesMenuInjection,
    editorInjector: TextEditorInjector,
    accountMenuInjector: AccountMenuInjector,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        notesMenuNavigation(
            notesMenuInjection = notesMenuInjection,
            navigateToNote = navController::navigateToNote,
            navigateToAccount = navController::navigateToAccount,
            navigateToNewNote = navController::navigateToNewNote
        )

        editorNavigation(
            editorInjector = editorInjector,
            navigateToNoteMenu = navController::navigateToNoteMenu,
            isUndoKeyEvent = isUndoKeyEvent,
        )

        accountMenuNavigation(
            accountMenuInjector = accountMenuInjector,
            navigateToAuthMenu = { }
        )

        builder()
    }
}



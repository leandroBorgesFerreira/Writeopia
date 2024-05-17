package io.writeopia.notes.desktop.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.KeyEvent
import io.writeopia.account.di.AccountMenuKmpInjector
import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.auth.core.token.MockTokenHandler
import io.writeopia.editor.di.EditorKmpInjector
import io.writeopia.model.ColorThemeOption
import io.writeopia.model.darkTheme
import io.writeopia.navigation.Navigation
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.note_menu.di.NotesMenuKmpInjection
import io.writeopia.note_menu.di.UiConfigurationInjector
import io.writeopia.sdk.network.injector.ConnectionInjector
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.theme.WrieopiaTheme
import io.writeopia.utils_module.Destinations
import kotlinx.coroutines.flow.StateFlow

@Composable
fun App(
    notesConfigurationInjector: NotesConfigurationInjector,
    repositoryInjection: RepositoryInjector,
    uiConfigurationInjector: UiConfigurationInjector,
    disableWebsocket: Boolean = false,
    selectionState: StateFlow<Boolean>,
    colorThemeOption: ColorThemeOption,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    selectColorTheme: (ColorThemeOption) -> Unit,
) {
    val authCoreInjection = KmpAuthCoreInjection()
    val connectionInjection =
        ConnectionInjector(
            bearerTokenHandler = MockTokenHandler,
            baseUrl = "https://writeopia.io/api",
            disableWebsocket = disableWebsocket
        )
    val editorInjector = EditorKmpInjector(
        authCoreInjection = authCoreInjection,
        repositoryInjection = repositoryInjection,
        connectionInjection = connectionInjection
    )
    val accountInjector = AccountMenuKmpInjector(authCoreInjection)

    val notesMenuInjection = NotesMenuKmpInjection(
        notesConfigurationInjector = notesConfigurationInjector,
        authCoreInjection = authCoreInjection,
        repositoryInjection = repositoryInjection,
        uiConfigurationInjector = uiConfigurationInjector,
        selectionState = selectionState,
    )

    WrieopiaTheme(darkTheme = colorThemeOption.darkTheme()) {
        Navigation(
            startDestination = Destinations.CHOOSE_NOTE.id,
            notesMenuInjection = notesMenuInjection,
            accountMenuInjector = accountInjector,
            editorInjector = editorInjector,
            isUndoKeyEvent = isUndoKeyEvent,
            selectColorTheme = selectColorTheme
        ) {

        }
    }
}

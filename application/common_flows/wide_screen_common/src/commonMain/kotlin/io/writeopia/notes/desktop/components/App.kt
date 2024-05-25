package io.writeopia.notes.desktop.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Composable
fun App(
    notesConfigurationInjector: NotesConfigurationInjector,
    repositoryInjection: RepositoryInjector,
    uiConfigurationInjector: UiConfigurationInjector,
    disableWebsocket: Boolean = false,
    selectionState: StateFlow<Boolean>,
    colorThemeOption: StateFlow<ColorThemeOption?>,
    coroutineScope: CoroutineScope,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    selectColorTheme: (ColorThemeOption) -> Unit,
) {
    val authCoreInjection = remember { KmpAuthCoreInjection() }
    val connectionInjection =
        remember {
            ConnectionInjector(
                bearerTokenHandler = MockTokenHandler,
                baseUrl = "https://writeopia.io/api",
                disableWebsocket = disableWebsocket
            )
        }
    val editorInjector = remember {
        EditorKmpInjector(
            authCoreInjection = authCoreInjection,
            repositoryInjection = repositoryInjection,
            connectionInjection = connectionInjection
        )
    }
    val accountInjector = remember { AccountMenuKmpInjector(authCoreInjection) }

    val notesMenuInjection = remember {
        NotesMenuKmpInjection(
            notesConfigurationInjector = notesConfigurationInjector,
            authCoreInjection = authCoreInjection,
            repositoryInjection = repositoryInjection,
            uiConfigurationInjector = uiConfigurationInjector,
            selectionState = selectionState,
        )
    }

    val colorTheme = colorThemeOption.collectAsState().value

    if (colorTheme != null) {
        WrieopiaTheme(darkTheme = colorThemeOption.collectAsState().value?.darkTheme() ?: false) {
            Navigation(
                startDestination = Destinations.CHOOSE_NOTE.id,
                notesMenuInjection = notesMenuInjection,
                accountMenuInjector = accountInjector,
                coroutineScope = coroutineScope,
                editorInjector = editorInjector,
                isUndoKeyEvent = isUndoKeyEvent,
                selectColorTheme = selectColorTheme
            ) {

            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

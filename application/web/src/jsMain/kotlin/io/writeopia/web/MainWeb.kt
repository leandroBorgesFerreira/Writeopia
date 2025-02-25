package io.writeopia.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.CanvasBasedWindow
import io.writeopia.notemenu.di.NotesInjector
import io.writeopia.notemenu.di.AndroidUiConfigurationInjector
import io.writeopia.notes.desktop.components.DesktopApp
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.image.ImageLoadConfig
import io.writeopia.ui.keyboard.KeyboardEvent
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow(title = "Writeopia") {
            ImageLoadConfig.configImageLoad()
            CreateAppInMemory(repositoryInjection = SqlDelightDaoInjector.noop())
        }
    }
}

@Composable
fun CreateAppInMemory(repositoryInjection: SqlDelightDaoInjector) {
    val coroutineScope = rememberCoroutineScope()
    val selectionState = MutableStateFlow(false)

    val uiConfigurationViewModel = AndroidUiConfigurationInjector()
        .provideUiConfigurationViewModel()

    val colorTheme =
        uiConfigurationViewModel.listenForColorTheme { "disconnected_user" }

    DesktopApp(
        notesInjector = NotesInjector.noop(),
        repositoryInjection = repositoryInjection,
        androidUiConfigurationInjector = AndroidUiConfigurationInjector(),
        selectionState = selectionState,
        isUndoKeyEvent = ::isUndoKeyboardEvent,
        colorThemeOption = colorTheme,
        selectColorTheme = uiConfigurationViewModel::changeColorTheme,
        coroutineScope = coroutineScope,
        keyboardEventFlow = MutableStateFlow(KeyboardEvent.IDLE),
        toggleMaxScreen = {}
    )
}

private fun isUndoKeyboardEvent(keyEvent: KeyEvent): Boolean =
    keyEvent.isMetaPressed &&
        keyEvent.key == Key.Z &&
        keyEvent.type == KeyEventType.KeyDown

private fun isSelectionKeyEventStart(keyEvent: KeyEvent): Boolean =
    (keyEvent.key == Key.MetaLeft || keyEvent.key == Key.MetaRight) &&
        keyEvent.type == KeyEventType.KeyDown

private fun isSelectionKeyEventStop(keyEvent: KeyEvent): Boolean =
    (keyEvent.key == Key.MetaLeft || keyEvent.key == Key.MetaRight) &&
        keyEvent.type == KeyEventType.KeyUp

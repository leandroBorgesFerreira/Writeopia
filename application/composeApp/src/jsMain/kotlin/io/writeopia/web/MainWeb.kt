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
import io.writeopia.notemenu.di.UiConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.keyboard.KeyboardEvent
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow(title = "Writeopia") {
            CreateAppInMemory(repositoryInjection = SqlDelightDaoInjector.noop())
        }
    }
}

@Composable
fun CreateAppInMemory(repositoryInjection: SqlDelightDaoInjector) {
    val coroutineScope = rememberCoroutineScope()
    val selectionState = MutableStateFlow(false)

    val uiConfigurationViewModel = UiConfigurationInjector()
        .provideUiConfigurationViewModel(coroutineScope = coroutineScope)

    val colorTheme =
        uiConfigurationViewModel.listenForColorTheme { "disconnected_user" }

    App(
        notesInjector = NotesInjector.noop(),
        repositoryInjection = repositoryInjection,
        uiConfigurationInjector = UiConfigurationInjector(),
        selectionState = selectionState,
        isUndoKeyEvent = ::isUndoKeyboardEvent,
        colorThemeOption = colorTheme,
        selectColorTheme = uiConfigurationViewModel::changeColorTheme,
        coroutineScope = coroutineScope,
        keyboardEventFlow = MutableStateFlow(KeyboardEvent.IDLE)
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

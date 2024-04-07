package io.writeopia.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.CanvasBasedWindow
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.drawer.factory.DefaultDrawersJs
import org.jetbrains.skiko.SkikoKey
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Writeopia") {
            CreateAppInMemory(repositoryInjection = SqlDelightDaoInjector.noop())
        }
    }
}

@Composable
fun CreateAppInMemory(repositoryInjection: SqlDelightDaoInjector) {
    App(
        notesConfigurationInjector = NotesConfigurationInjector.noop(),
        repositoryInjection = repositoryInjection,
        drawersFactory = DefaultDrawersJs,
        modifier = { writeopiaStateManager ->
            Modifier.onPreviewKeyEvent { keyEvent ->
                val shouldHandle = keyEvent.isMetaPressed &&
                        keyEvent.nativeKeyEvent.key == SkikoKey.KEY_Z &&
                        keyEvent.type == KeyEventType.KeyDown

                if (shouldHandle) {
                    writeopiaStateManager.undo()
                    true
                } else {
                    false
                }
            }
        }
    )
}


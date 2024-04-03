package io.writeopia.web

import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Writeopia") {
            Text("oi!")
//            val database = createDatabase(DriverFactory(), url = "")
//            App(
//                notesConfigurationInjector = NotesConfigurationInjector(database),
//                repositoryInjection = SqlDelightDaoInjector(database),
//                drawersFactory = DefaultDrawersJs
//            )
        }
    }
}


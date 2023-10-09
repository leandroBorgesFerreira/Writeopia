import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.document
import kotlinx.dom.appendText
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
//    Text("Heeeeeyyyyy")

    document.body?.appendText("Hello, world JS!")

    onWasmReady {

    }
}
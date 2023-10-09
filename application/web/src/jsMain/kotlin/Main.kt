import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import io.writeopia.sdk.WriteopiaEditor
import io.writeopia.sdk.WritepiaTag
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.content.*
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.models.story.StoryTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Compose Rich Editor") {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("putz!!!")
                CreateTextEditor()
            }
        }
    }
}


@Composable
fun CreateTextEditor() {
    val writeopiaManager = WriteopiaManager(dispatcher = Dispatchers.Main).apply {
        newStory()
    }

    TextEditor(drawers = drawers(), drawState = writeopiaManager.toDraw)
}

@Composable
fun TextEditor(
    drawState: Flow<DrawState>,
    drawers: Map<Int, StoryStepDrawer>
) {
    val toDraw by drawState.collectAsState(DrawState())

    WriteopiaEditor(modifier = Modifier.fillMaxSize(), drawers = drawers, storyState = toDraw)
}

@Composable
fun drawers(): Map<Int, HeaderDrawer> {
    return buildMap {
        put(
            StoryTypes.TITLE.type.number,
            HeaderDrawer(
                drawer = {
                    TitleDrawer(
                        containerModifier = Modifier.align(Alignment.BottomStart),
                    )
                },
            )
        )
    }
}


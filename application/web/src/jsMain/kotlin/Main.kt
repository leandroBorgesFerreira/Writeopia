import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import io.writeopia.sdk.WriteopiaEditor
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.content.HeaderDrawer
import io.writeopia.sdk.drawer.content.TitleDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Compose Rich Editor") {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Write your text bellow", modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge)

                Card(modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 30.dp)) {
                    CreateTextEditor()
                }
            }
        }
    }
}


@Composable
fun CreateTextEditor() {
    val now = Clock.System.now()

    val writeopiaManager = WriteopiaManager(dispatcher = Dispatchers.Main).apply {
        initDocument(
            Document(
                title = "Sample Document",
                createdAt = now,
                lastUpdatedAt = now,
                userId = "",
                content = supermarketList()
            )
        )
    }

    TextEditor(drawers = DefaultDrawersJs.create(writeopiaManager), drawState = writeopiaManager.toDraw)
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


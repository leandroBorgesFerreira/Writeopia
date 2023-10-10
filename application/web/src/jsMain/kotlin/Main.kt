import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import io.writeopia.sdk.WriteopiaEditorBox
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.models.document.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Compose Rich Editor") {
            Column(
                Modifier.fillMaxSize().background(Color.Gray).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    "Write your text bellow",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.titleLarge,
                )

                Column(
                    modifier = Modifier
                        .width(1000.dp)
                        .defaultMinSize(minHeight = 700.dp)
                        .padding(start = 30.dp, end = 30.dp, bottom = 30.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                ) {
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
    WriteopiaEditorBox(modifier = Modifier, drawers = drawers, storyState = toDraw)
}

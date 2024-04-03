import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.ui.WriteopiaEditor
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.factory.DefaultDrawersJs
import io.writeopia.ui.manager.WriteopiaStateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Writeopia") {
            WriteopiaApp()
        }
    }
}

@Composable
private fun WriteopiaApp() {
    val writeopiaManager = WriteopiaStateManager.create(
        dispatcher = Dispatchers.Main,
        writeopiaManager = WriteopiaManager()
    ).apply {
        newStory()
    }

    Column(
        Modifier
            .clickable(
                onClick = writeopiaManager::clickAtTheEnd,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            )
            .fillMaxSize()
            .background(Color.Gray),
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
            CreateTextEditor(writeopiaManager)

            Box(
                modifier = Modifier.weight(1F)
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            writeopiaManager.clickAtTheEnd()
                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
            )
        }
    }
}

@Composable
private fun CreateTextEditor(writeopiaManager: WriteopiaStateManager) {
    val listState: LazyListState = rememberLazyListState()
    val position by writeopiaManager.scrollToPosition.collectAsState()

    LaunchedEffect(position) {
        writeopiaManager.scrollToPosition.collectLatest {
            listState.animateScrollBy(70F)
        }
    }

    TextEditor(
        lazyListState = listState,
        drawers = DefaultDrawersJs.create(writeopiaManager),
        drawState = writeopiaManager.toDraw
    )
}

@Composable
private fun TextEditor(
    lazyListState: LazyListState,
    drawState: Flow<DrawState>,
    drawers: Map<Int, StoryStepDrawer>
) {
    val toDraw by drawState.collectAsState(DrawState())
    WriteopiaEditor(
        modifier = Modifier,
        drawers = drawers,
        storyState = toDraw,
        listState = lazyListState
    )
}

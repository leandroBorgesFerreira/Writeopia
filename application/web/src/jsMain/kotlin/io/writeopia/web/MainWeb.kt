package io.writeopia.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.writeopia.note_menu.di.NotesConfigurationInjector
import io.writeopia.notes.desktop.components.App
import io.writeopia.sqldelight.database.createDatabase
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import io.writeopia.ui.drawer.factory.DefaultDrawersJs
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow("Writeopia") {
            val database = createDatabase(DriverFactory(), url = "")
            App(
                notesConfigurationInjector = NotesConfigurationInjector(database),
                repositoryInjection = SqlDelightDaoInjector(database),
                drawersFactory = DefaultDrawersJs
            )
        }
    }
}
//
//@Composable
//private fun WriteopiaApp() {
//    val writeopiaManager = WriteopiaStateManager.create(
//        dispatcher = Dispatchers.Main,
//        writeopiaManager = WriteopiaManager()
//    ).apply {
//        newStory()
//    }
//
//    Column(
//        Modifier
//            .clickable(
//                onClick = writeopiaManager::clickAtTheEnd,
//                indication = null,
//                interactionSource = remember { MutableInteractionSource() },
//            )
//            .fillMaxSize()
//            .background(Color.Gray),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Text(
//            "Write your text bellow",
//            modifier = Modifier.padding(20.dp),
//            style = MaterialTheme.typography.titleLarge,
//        )
//
//        Column(
//            modifier = Modifier
//                .width(1000.dp)
//                .defaultMinSize(minHeight = 700.dp)
//                .padding(start = 30.dp, end = 30.dp, bottom = 30.dp)
//                .clip(RoundedCornerShape(20.dp))
//                .background(Color.White)
//        ) {
//            CreateTextEditor(writeopiaManager)
//
//            Box(
//                modifier = Modifier.weight(1F)
//                    .fillMaxWidth()
//                    .clickable(
//                        onClick = {
//                            writeopiaManager.clickAtTheEnd()
//                        },
//                        interactionSource = remember { MutableInteractionSource() },
//                        indication = null,
//                    )
//            )
//        }
//    }
//}
//
//@Composable
//private fun CreateTextEditor(writeopiaManager: WriteopiaStateManager) {
//    val listState: LazyListState = rememberLazyListState()
//    val position by writeopiaManager.scrollToPosition.collectAsState()
//
//    LaunchedEffect(position) {
//        writeopiaManager.scrollToPosition.collectLatest {
//            listState.animateScrollBy(70F)
//        }
//    }
//
//    TextEditor(
//        lazyListState = listState,
//        drawers = DefaultDrawersJs.create(writeopiaManager),
//        drawState = writeopiaManager.toDraw
//    )
//}
//
//@Composable
//private fun TextEditor(
//    lazyListState: LazyListState,
//    drawState: Flow<DrawState>,
//    drawers: Map<Int, StoryStepDrawer>
//) {
//    val toDraw by drawState.collectAsState(DrawState())
//    WriteopiaEditor(
//        modifier = Modifier,
//        drawers = drawers,
//        storyState = toDraw,
//        listState = lazyListState
//    )
//}

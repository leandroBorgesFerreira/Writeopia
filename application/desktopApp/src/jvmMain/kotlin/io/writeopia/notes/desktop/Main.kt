package io.writeopia.notes.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.sdk.WriteopiaEditor
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.factory.DefaultDrawersDesktop
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.persistence.sqldelight.DriverFactory
import io.writeopia.sdk.persistence.sqldelight.di.SqlDelightDaoInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Writeopia for Desktop",
        state = rememberWindowState(width = 1100.dp, height = 800.dp)
    ) {
        App()
    }
}

@Composable
fun App() {
    val writeopiaManager = WriteopiaManager(dispatcher = Dispatchers.IO).apply {
        newStory()
    }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            //Todo: Move this to WriteopiaEditorBox
            BoxWithConstraints {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .let { modifierLet ->
                            if (maxWidth > 900.dp) {
                                modifierLet.width(1000.dp)
                            } else {
                                modifierLet.fillMaxWidth()
                            }
                        }
                        .defaultMinSize(minHeight = 700.dp)
                        .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 30.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White),
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
    }
}


@Composable
private fun CreateTextEditor(manager: WriteopiaManager) {
    val listState: LazyListState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    coroutine.launch {
        manager.scrollToPosition.collectLatest {
            listState.animateScrollBy(70F)
        }
    }

    TextEditor(lazyListState = listState, drawers = DefaultDrawersDesktop.create(manager), drawState = manager.toDraw)
}

@Composable
private fun TextEditor(
    lazyListState: LazyListState,
    drawState: Flow<DrawState>,
    drawers: Map<Int, StoryStepDrawer>
) {
    val toDraw by drawState.collectAsState(DrawState())
    WriteopiaEditor(
        modifier = Modifier.fillMaxWidth(),
        drawers = drawers,
        storyState = toDraw,
        listState = lazyListState
    )
}

private fun createPersistence() {
    SqlDelightDaoInjector(DriverFactory()).provideDocumentDao()
}
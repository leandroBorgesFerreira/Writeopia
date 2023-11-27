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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.auth.core.di.KmpAuthCoreInjection
import io.writeopia.editor.di.EditorKmpInjector
import io.writeopia.editor.ui.TextEditor
import io.writeopia.sdk.drawer.factory.DefaultDrawersDesktop
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.persistence.core.di.DaosInjector
import io.writeopia.sdk.persistence.core.tracker.OnUpdateDocumentTracker
import io.writeopia.sqldelight.create
import kotlinx.coroutines.Dispatchers
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
    val authCoreInjection = KmpAuthCoreInjection()
    val daosInjection = DaosInjector.create()

    val editorInjetor = EditorKmpInjector(
        authCoreInjection = authCoreInjection,
        daosInjection = daosInjection
    )

    val writeopiaManager = WriteopiaManager(dispatcher = Dispatchers.IO).apply {
        saveOnStoryChanges(OnUpdateDocumentTracker(daosInjection.provideDocumentDao()))
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
                    CreateTextEditor(writeopiaManager, editorInjetor)

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
private fun CreateTextEditor(manager: WriteopiaManager, editorKmpInjector: EditorKmpInjector) {
    val listState: LazyListState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    coroutine.launch {
        manager.scrollToPosition.collectLatest {
            listState.animateScrollBy(70F)
        }
    }

    val viewModel = editorKmpInjector.provideNoteDetailsViewModel()
    viewModel.initCoroutine(coroutine)
    manager.newStory()

    Column(modifier = Modifier.fillMaxWidth()) {
        TextEditor(viewModel, DefaultDrawersDesktop)
    }
}


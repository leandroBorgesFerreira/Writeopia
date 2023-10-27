package io.writeopia.notes.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.writeopia.sdk.WriteopiaEditor
import io.writeopia.sdk.WriteopiaEditorBox
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.drawer.factory.DefaultDrawersDesktop
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.serialization.data.DecorationApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.lang.System.Logger

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Writeopia for Desktop",
        state = rememberWindowState(width = 1100.dp, height = 800.dp)
    ) {
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
}


@Composable
fun CreateTextEditor(manager: WriteopiaManager) {
    TextEditor(drawers = DefaultDrawersDesktop.create(manager), drawState = manager.toDraw)
}

@Composable
fun TextEditor(
    drawState: Flow<DrawState>,
    drawers: Map<Int, StoryStepDrawer>
) {
    val toDraw by drawState.collectAsState(DrawState())
    WriteopiaEditor(modifier = Modifier.fillMaxWidth(), drawers = drawers, storyState = toDraw)
}
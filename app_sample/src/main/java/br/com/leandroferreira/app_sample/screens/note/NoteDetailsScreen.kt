package br.com.leandroferreira.app_sample.screens.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.app_sample.screens.note.input.InputScreen
import br.com.leandroferreira.app_sample.theme.ApplicationComposeTheme
import br.com.leandroferreira.storyteller.StoryTellerTimeline
import br.com.leandroferreira.storyteller.drawer.DefaultDrawers

@Composable
fun NoteDetailsScreen(documentId: String, noteDetailsViewModel: NoteDetailsViewModel) {
    noteDetailsViewModel.requestDocumentContent(documentId)

    ApplicationComposeTheme {
        Scaffold(
            topBar = { TopBar(noteDetailsViewModel = noteDetailsViewModel) },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                Body(noteDetailsViewModel)

                InputScreen(
                    onBackPress = noteDetailsViewModel::undo,
                    canUndoState = noteDetailsViewModel.canUndo,
                    canRedoState = noteDetailsViewModel.canRedo
                )
            }
        }
    }
}

@Composable
private fun TopBar(noteDetailsViewModel: NoteDetailsViewModel) {
    TopAppBar(
        title = { Text(text = "Note") },
        actions = {
            TextButton(onClick = noteDetailsViewModel::saveNote) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 19.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                )
            }
        }
    )
}

@Composable
fun ColumnScope.Body(noteDetailsViewModel: NoteDetailsViewModel) {
    val storyState by noteDetailsViewModel.story.collectAsStateWithLifecycle()
    val editable by noteDetailsViewModel.editModeState.collectAsStateWithLifecycle()

    StoryTellerTimeline(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F),
        storyState = storyState,
        contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
        editable = editable,
//            listState = listState,
        drawers = DefaultDrawers.create(editable, noteDetailsViewModel.storyTellerManager)
    )
}



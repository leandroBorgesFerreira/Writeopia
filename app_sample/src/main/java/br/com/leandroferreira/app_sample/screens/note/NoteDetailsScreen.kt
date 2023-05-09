package br.com.leandroferreira.app_sample.screens.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.app_sample.theme.ApplicationComposeTheme
import br.com.leandroferreira.storyteller.StoryTellerTimeline
import br.com.leandroferreira.storyteller.drawer.DefaultDrawers

@Composable
fun NoteDetailsScreen(documentId: String, noteDetailsViewModel: NoteDetailsViewModel) {
    noteDetailsViewModel.requestDocumentContent(documentId)

    ApplicationComposeTheme {
        Scaffold(
            topBar = { TopBar() },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                Body(noteDetailsViewModel)
            }
        }
    }
}

@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(text = "Note") },
    )
}

@Composable
fun Body(noteDetailsViewModel: NoteDetailsViewModel) {
    val storyState by noteDetailsViewModel.story.collectAsStateWithLifecycle()

    StoryTellerTimeline(
        modifier = Modifier.fillMaxSize(),
        storyState = storyState,
        contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
        editable = false,
//            listState = listState,
        drawers = DefaultDrawers.create(false, noteDetailsViewModel.storyTellerManager)
    )
}



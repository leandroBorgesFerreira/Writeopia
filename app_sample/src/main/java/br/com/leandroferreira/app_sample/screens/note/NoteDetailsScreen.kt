package br.com.leandroferreira.app_sample.screens.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.app_sample.screens.note.input.InputScreen
import br.com.leandroferreira.storyteller.StoryTellerTimeline
import br.com.leandroferreira.storyteller.drawer.DefaultDrawers
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NoteDetailsScreen(documentId: String, noteDetailsViewModel: NoteDetailsViewModel) {
    noteDetailsViewModel.requestDocumentContent(documentId)

    Scaffold(
        topBar = { TopBar() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .imePadding()
        ) {
            Body(noteDetailsViewModel)

            InputScreen(
                onBackPress = noteDetailsViewModel::undo,
                onForwardPress = noteDetailsViewModel::redo,
                canUndoState = noteDetailsViewModel.canUndo,
                canRedoState = noteDetailsViewModel.canRedo
            )
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
fun ColumnScope.Body(noteDetailsViewModel: NoteDetailsViewModel) {
    val storyState by noteDetailsViewModel.story.collectAsStateWithLifecycle()
    val editable by noteDetailsViewModel.editModeState.collectAsStateWithLifecycle()
    val listState: LazyListState = rememberLazyListState()
    val position by noteDetailsViewModel.scrollToPosition.collectAsStateWithLifecycle()

    if (position != null) {
        //Todo: Review this. Is a LaunchedEffect the correct way to do this??
        LaunchedEffect(position, block = {
            noteDetailsViewModel.scrollToPosition.collectLatest {
                listState.animateScrollToItem(position!!, scrollOffset = 100)
            }
        })
    }

    OnLifecycleEvent { _, event ->
        // do stuff on event

        if (event == Lifecycle.Event.ON_PAUSE) {
            noteDetailsViewModel.saveNote()
        }
    }

    StoryTellerTimeline(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F),
        storyState = storyState,
        contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
        editable = editable,
        listState = listState,
        drawers = DefaultDrawers.create(editable, noteDetailsViewModel.storyTellerManager)
    )
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

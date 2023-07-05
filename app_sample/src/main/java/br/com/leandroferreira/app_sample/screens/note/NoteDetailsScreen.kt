package br.com.leandroferreira.app_sample.screens.note

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.app_sample.screens.note.input.InputScreen
import br.com.leandroferreira.app_sample.theme.BACKGROUND_VARIATION
import br.com.leandroferreira.app_sample.theme.BACKGROUND_VARIATION_DARK
import com.github.leandroborgesferreira.storyteller.StoryTellerEditor
import com.github.leandroborgesferreira.storyteller.drawer.DefaultDrawers
import com.github.leandroborgesferreira.storyteller.uicomponents.EditionScreen
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    documentId: String?,
    title: String?,
    noteDetailsViewModel: NoteDetailsViewModel,
    navigateBack: () -> Unit,
) {
    val navController = rememberNavController()

    if (documentId != null) {
        noteDetailsViewModel.requestDocumentContent(documentId)
    } else {
        noteDetailsViewModel.createNewNote(
            UUID.randomUUID().toString(),
            stringResource(R.string.untitled)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title?.takeIf { it.isNotBlank() }
                            ?: stringResource(id = R.string.note),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = navigateBack)
                            .padding(10.dp),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .imePadding()
        ) {
            TextEditor(noteDetailsViewModel)

            BottomScreen(noteDetailsViewModel)
        }
    }
}

@Composable
fun ColumnScope.TextEditor(noteDetailsViewModel: NoteDetailsViewModel) {
    val storyState by noteDetailsViewModel.toDraw.collectAsStateWithLifecycle()
    val editable by noteDetailsViewModel.editModeState.collectAsStateWithLifecycle()
    val listState: LazyListState = rememberLazyListState()
    val position by noteDetailsViewModel.scrollToPosition.collectAsStateWithLifecycle()

    if (position != null) {
        //Todo: Review this. Is a LaunchedEffect the correct way to do this??
        LaunchedEffect(position, block = {
            noteDetailsViewModel.scrollToPosition.collectLatest {
                listState.animateScrollToItem(position!!, scrollOffset = -100)
            }
        })
    }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            noteDetailsViewModel.saveNote()
        }
    }

    StoryTellerEditor(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F),
        storyState = storyState,
        editable = editable,
        listState = listState,
        drawers = DefaultDrawers.create(
            editable,
            noteDetailsViewModel.storyTellerManager,
            groupsBackgroundColor = if (isSystemInDarkTheme()) {
                BACKGROUND_VARIATION_DARK
            } else {
                BACKGROUND_VARIATION
            }
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomScreen(noteDetailsViewModel: NoteDetailsViewModel) {
    val editState by noteDetailsViewModel.isEditState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = editState,
        label = "bottomSheetAnimation",
        transitionSpec = {
            slideInVertically(
                animationSpec = tween(durationMillis = 130),
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn() with slideOutVertically(
                animationSpec = tween(durationMillis = 130),
                targetOffsetY = { fullHeight -> fullHeight }
            )
        }
    ) { isEdit ->
        val topCorner = CornerSize(10.dp)
        val bottomCorner = CornerSize(0.dp)

        if (isEdit) {
            EditionScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topCorner,
                            topCorner,
                            bottomCorner,
                            bottomCorner
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary),
                onDelete = noteDetailsViewModel::deleteSelection
            )
        } else {
            InputScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topCorner,
                            topCorner,
                            bottomCorner,
                            bottomCorner
                        )
                    ),
                onBackPress = noteDetailsViewModel::undo,
                onForwardPress = noteDetailsViewModel::redo,
                canUndoState = noteDetailsViewModel.canUndo,
                canRedoState = noteDetailsViewModel.canRedo
            )
        }
    }
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

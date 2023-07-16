package br.com.leandroferreira.editor

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.editor.input.InputScreen
import br.com.leandroferreira.resourcers.R
import com.github.leandroborgesferreira.storyteller.StoryTellerEditor
import com.github.leandroborgesferreira.storyteller.drawer.DefaultDrawers
import com.github.leandroborgesferreira.storyteller.uicomponents.EditionScreen
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

const val NAVIGATE_BACK_TEST_TAG = "NoteEditorScreenNavigateBack"
const val NOTE_EDITION_SCREEN_TITLE_TEST_TAG = "noteEditionScreenTitle"

@Composable
fun NoteEditorScreen(
    documentId: String?,
    title: String?,
    noteEditorViewModel: NoteEditorViewModel,
    navigateBack: () -> Unit,
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                noteEditorViewModel.removeNoteIfEmpty(onComplete = navigateBack)
            }
        }
    }

    DisposableEffect(key1 = backDispatcher) {
        backDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }

    if (documentId != null) {
        noteEditorViewModel.requestDocumentContent(documentId)
    } else {
        noteEditorViewModel.createNewDocument(
            UUID.randomUUID().toString(),
            stringResource(R.string.untitled)
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                title = title?.takeIf { it.isNotBlank() } ?: stringResource(id = R.string.note),
                navigationClick = {
                    noteEditorViewModel.removeNoteIfEmpty(onComplete = navigateBack)
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
            TextEditor(noteEditorViewModel)

            BottomScreen(noteEditorViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, navigationClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier.height(50.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.semantics {
                        testTag = NOTE_EDITION_SCREEN_TITLE_TEST_TAG
                    },
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        navigationIcon = {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .semantics {
                            testTag = NAVIGATE_BACK_TEST_TAG
                        }
                        .clip(CircleShape)
                        .clickable(onClick = navigationClick)
                        .padding(10.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun ColumnScope.TextEditor(noteEditorViewModel: NoteEditorViewModel) {
    val storyState by noteEditorViewModel.toDraw.collectAsStateWithLifecycle()
    val editable by noteEditorViewModel.editModeState.collectAsStateWithLifecycle()
    val listState: LazyListState = rememberLazyListState()
    val position by noteEditorViewModel.scrollToPosition.collectAsStateWithLifecycle()

    if (position != null) {
        //Todo: Review this. Is a LaunchedEffect the correct way to do this??
        LaunchedEffect(position, block = {
            noteEditorViewModel.scrollToPosition.collectLatest {
                listState.animateScrollBy(70F)
            }
        })
    }

    val clipShape = MaterialTheme.shapes.medium

    StoryTellerEditor(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F),
        storyState = storyState,
        editable = editable,
        listState = listState,
        drawers = DefaultDrawers.create(
            editable,
            noteEditorViewModel.storyTellerManager,
            defaultBorder = clipShape
//            groupsBackgroundColor = MaterialTheme.colorScheme.surface
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomScreen(noteEditorViewModel: NoteEditorViewModel) {
    val editState by noteEditorViewModel.isEditState.collectAsStateWithLifecycle()

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
                onDelete = noteEditorViewModel::deleteSelection
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
                onBackPress = noteEditorViewModel::undo,
                onForwardPress = noteEditorViewModel::redo,
                canUndoState = noteEditorViewModel.canUndo,
                canRedoState = noteEditorViewModel.canRedo
            )
        }
    }
}

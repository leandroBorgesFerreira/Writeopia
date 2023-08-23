package com.github.leandroborgesferreira.storytellerapp.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.leandroborgesferreira.storytellerapp.editor.configuration.ui.HeaderEdition
import com.github.leandroborgesferreira.storytellerapp.editor.input.InputScreen
import com.github.leandroborgesferreira.storytellerapp.editor.model.EditState
import com.github.leandroborgesferreira.storytellerapp.appresourcers.R
import com.github.leandroborgesferreira.storyteller.StoryTellerEditor
import com.github.leandroborgesferreira.storyteller.drawer.DefaultDrawers
import com.github.leandroborgesferreira.storyteller.uicomponents.EditionScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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

    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.background
    val systemBarDefaultColor = MaterialTheme.colorScheme.primary

    DisposableEffect(systemUiController) {
        systemUiController.setStatusBarColor(color = systemBarColor)
        onDispose {}
    }

    BackHandler {
        noteEditorViewModel.removeNoteIfEmpty(onComplete = {
            systemUiController.setStatusBarColor(color = systemBarDefaultColor)
            navigateBack()
        })
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
                    systemUiController.setStatusBarColor(color = systemBarDefaultColor)
                    noteEditorViewModel.removeNoteIfEmpty(onComplete = navigateBack)
                },
                shareDocument = noteEditorViewModel::onMoreOptionsClick
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .imePadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TextEditor(noteEditorViewModel)

                BottomScreen(noteEditorViewModel)
            }

            val colors = listOf(
                Color.Blue.toArgb(),
                Color.Yellow.toArgb(),
                Color.DarkGray.toArgb(),
                Color.Red.toArgb(),
                Color.Magenta.toArgb(),
                Color.Gray.toArgb(),
                Color.Green.toArgb(),
                Color.Cyan.toArgb(),
                Color.Black.toArgb(),
                Color.White.toArgb(),
            )

            val headerEdition by noteEditorViewModel.editHeader.collectAsStateWithLifecycle()

            HeaderEdition(
                modifier = Modifier.fillMaxWidth(),
                availableColors = colors,
                onColorSelection = noteEditorViewModel::onHeaderColorSelection,
                outsideClick = noteEditorViewModel::onHeaderEditionCancel,
                visibilityState = headerEdition
            )

            val showGlobalMenu by noteEditorViewModel.showGlobalMenu.collectAsStateWithLifecycle()

            val context = LocalContext.current

            AnimatedVisibility(
                visible = showGlobalMenu,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }
                )
            ) {
                NoteGlobalActionsMenu {
                    noteEditorViewModel.shareDocumentInJson(context)
                }
            }
        }
    }
}

@Preview
@Composable
private fun NoteGlobalActionsMenu(onShare: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onShare)
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            text = "Export as Json",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String = "",
    navigationClick: () -> Unit = {},
    shareDocument: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.height(44.dp),
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
                    fontSize = 18.sp
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
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = shareDocument)
                    .padding(9.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview
@Composable
private fun TopBar_Preview() {
    Box(modifier = Modifier.background(Color.LightGray)) {
        TopBar(title = "Title", shareDocument = {})
    }
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
            defaultBorder = clipShape,
            onHeaderClick = noteEditorViewModel::onHeaderClick
//            groupsBackgroundColor = MaterialTheme.colorScheme.surface
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomScreen(noteEditorViewModel: NoteEditorViewModel) {
    val editState by noteEditorViewModel.isEditState.collectAsStateWithLifecycle()

    val topCorner = CornerSize(10.dp)
    val bottomCorner = CornerSize(0.dp)

    val containerModifier = Modifier
        .fillMaxWidth()
        .clip(
            RoundedCornerShape(
                topCorner,
                topCorner,
                bottomCorner,
                bottomCorner
            )
        )
        .background(MaterialTheme.colorScheme.primary)

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
            ) + fadeOut()
        }
    ) { editStateAnimated ->

        when (editStateAnimated) {
            EditState.TEXT -> {
                InputScreen(
                    modifier = containerModifier,
                    onBackPress = noteEditorViewModel::undo,
                    onForwardPress = noteEditorViewModel::redo,
                    canUndoState = noteEditorViewModel.canUndo,
                    canRedoState = noteEditorViewModel.canRedo
                )
            }

            EditState.SELECTED_TEXT -> {
                EditionScreen(
                    modifier = containerModifier,
                    onDelete = noteEditorViewModel::deleteSelection
                )
            }
        }
    }
}


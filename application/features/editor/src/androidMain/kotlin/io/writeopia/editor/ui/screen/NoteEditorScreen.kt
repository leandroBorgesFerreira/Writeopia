package io.writeopia.editor.ui.screen

//import androidx.compose.ui.tooling.preview.Preview
//import io.writeopia.appresourcers.R
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.writeopia.editor.configuration.ui.HeaderEdition
import io.writeopia.editor.configuration.ui.NoteGlobalActionsMenu
import io.writeopia.editor.input.InputScreen
import io.writeopia.editor.model.EditState
import io.writeopia.editor.ui.TextEditor
import io.writeopia.editor.viewmodel.NoteEditorViewModel
import io.writeopia.editor.viewmodel.ShareDocument
import io.writeopia.ui.drawer.factory.DefaultDrawersAndroid
import io.writeopia.sdk.models.id.GenerateId
import kotlinx.coroutines.flow.StateFlow

const val NAVIGATE_BACK_TEST_TAG = "NoteEditorScreenNavigateBack"
const val NOTE_EDITION_SCREEN_TITLE_TEST_TAG = "noteEditionScreenTitle"

@Composable
internal fun NoteEditorScreen(
    documentId: String?,
    title: String?,
    noteEditorViewModel: NoteEditorViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.background
    val systemBarDefaultColor = MaterialTheme.colorScheme.primary

    DisposableEffect(systemUiController) {
        systemUiController.setStatusBarColor(color = systemBarColor)
        onDispose {}
    }

    BackHandler {
        noteEditorViewModel.handleBackAction(navigateBack = {
            systemUiController.setStatusBarColor(color = systemBarDefaultColor)
            navigateBack()
        })
    }

    if (documentId != null) {
        noteEditorViewModel.loadDocument(documentId)
    } else {
        noteEditorViewModel.createNewDocument(
            GenerateId.generate(),
            "Untitled",
//            stringResource(R.string.untitled)
        )
    }

    val context = LocalContext.current
    val document = noteEditorViewModel.documentToShareInfo.collectAsState().value

    if (document != null) {
        LaunchedEffect(document.hashCode()) {
            shareDocument(context, document)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                titleState = noteEditorViewModel.currentTitle,
                navigationClick = {
                    systemUiController.setStatusBarColor(color = systemBarDefaultColor)
                    noteEditorViewModel.handleBackAction(navigateBack = navigateBack)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TextEditor(noteEditorViewModel, DefaultDrawersAndroid, Modifier.weight(1F))

                BottomScreen(
                    noteEditorViewModel.isEditState,
                    noteEditorViewModel::undo,
                    noteEditorViewModel::redo,
                    noteEditorViewModel.canUndo,
                    noteEditorViewModel.canRedo,
                    noteEditorViewModel::deleteSelection
                )
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

            val headerEdition by noteEditorViewModel.editHeader.collectAsState()

            HeaderEdition(
                modifier = Modifier.fillMaxWidth(),
                availableColors = colors,
                onColorSelection = noteEditorViewModel::onHeaderColorSelection,
                outsideClick = noteEditorViewModel::onHeaderEditionCancel,
                visibilityState = headerEdition
            )

            val showGlobalMenu by noteEditorViewModel.showGlobalMenu.collectAsState()

            AnimatedVisibility(
                visible = showGlobalMenu,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }
                )
            ) {
                NoteGlobalActionsMenu(
                    onShareJson = { noteEditorViewModel.shareDocumentInJson() },
                    onShareMd = { noteEditorViewModel.shareDocumentInMarkdown() }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    titleState: StateFlow<String>,
    modifier: Modifier = Modifier,
    navigationClick: () -> Unit = {},
    shareDocument: () -> Unit
) {
    val title by titleState.collectAsState()

    TopAppBar(
        modifier = modifier.height(44.dp),
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
                    contentDescription = "",
//                    stringResource(R.string.back),
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

private fun shareDocument(context: Context, shareDocument: ShareDocument) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(Intent.EXTRA_TEXT, shareDocument.content)
        putExtra(Intent.EXTRA_TITLE, shareDocument.title)
        action = Intent.ACTION_SEND
        this.type = shareDocument.type
    }

    context.startActivity(
        Intent.createChooser(intent, "Export Document")
    )
}

//@Preview
//@Composable
//private fun TopBar_Preview() {
//    Box(modifier = Modifier.background(Color.LightGray)) {
//        TopBar(titleState = MutableStateFlow("Title"), shareDocument = {})
//    }
//}

@Composable
private fun BottomScreen(
    editState: StateFlow<EditState>,
    unDo: () -> Unit = {},
    reDo: () -> Unit = {},
    canUndo: StateFlow<Boolean>,
    canRedo: StateFlow<Boolean>,
    deleteSelection: () -> Unit = {}
) {
    val edit by editState.collectAsState()

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
        targetState = edit,
        label = "bottomSheetAnimation",
        transitionSpec = {
            (slideInVertically(
                animationSpec = tween(durationMillis = 130),
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn()) togetherWith slideOutVertically(
                animationSpec = tween(durationMillis = 130),
                targetOffsetY = { fullHeight -> fullHeight }
            ) + fadeOut()
        }
    ) { editStateAnimated ->

        when (editStateAnimated) {
            EditState.TEXT -> {
                InputScreen(
                    modifier = containerModifier,
                    onBackPress = unDo,
                    onForwardPress = reDo,
                    canUndoState = canUndo,
                    canRedoState = canRedo,
                )
            }

            EditState.SELECTED_TEXT -> {
                io.writeopia.ui.components.EditionScreen(
                    modifier = containerModifier,
                    onDelete = deleteSelection
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun BottomScreenTextPreview() {
//    BottomScreen(
//        editState = MutableStateFlow(EditState.TEXT),
//        canUndo = MutableStateFlow(true),
//        canRedo = MutableStateFlow(true),
//    )
//}
//
//@Preview
//@Composable
//private fun BottomScreenSelectedPreview() {
//    BottomScreen(
//        editState = MutableStateFlow(EditState.SELECTED_TEXT),
//        canUndo = MutableStateFlow(true),
//        canRedo = MutableStateFlow(true),
//    )
//}

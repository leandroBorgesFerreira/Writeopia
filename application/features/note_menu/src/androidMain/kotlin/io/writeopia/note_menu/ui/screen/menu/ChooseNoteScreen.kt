package io.writeopia.note_menu.ui.screen.menu

//import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.valentinilk.shimmer.shimmer
import io.writeopia.note_menu.ui.screen.configuration.ConfigurationsMenu
import io.writeopia.note_menu.ui.screen.configuration.NotesSelectionMenu
import io.writeopia.note_menu.viewmodel.ChooseNoteViewModel
import io.writeopia.note_menu.viewmodel.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun ChooseNoteScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    navigateToAccount: () -> Unit,
    newNote: () -> Unit,
) {
    LaunchedEffect(key1 = "refresh", block = {
        chooseNoteViewModel.requestDocuments(false)
        // Todo: Remove BuildConfig.DEBUG check later.
        chooseNoteViewModel.requestUser()
    })

    val hasSelectedNotes by chooseNoteViewModel.hasSelectedNotes.collectAsState()
    val editState by chooseNoteViewModel.editState.collectAsState()

    BackHandler(hasSelectedNotes || editState) {
        when {
            editState -> {
                chooseNoteViewModel.cancelMenu()
            }

            hasSelectedNotes -> {
                chooseNoteViewModel.clearSelection()
            }
        }
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopBar(
                        titleState = chooseNoteViewModel.userName,
                        accountClick = navigateToAccount,
                        menuClick = chooseNoteViewModel::editMenu
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(newNoteClick = newNote)
                }
            ) { paddingValues ->
                Content(
                    chooseNoteViewModel = chooseNoteViewModel,
                    navigateToNote = navigateToNote,
                    selectionListener = chooseNoteViewModel::onDocumentSelected,
                    paddingValues = paddingValues,
                )
            }

            NotesSelectionMenu(
                visibilityState = hasSelectedNotes,
                onCopy = chooseNoteViewModel::copySelectedNotes,
                onFavorite = chooseNoteViewModel::favoriteSelectedNotes,
                onDelete = chooseNoteViewModel::deleteSelectedNotes,
            )

            ConfigurationsMenu(
                visibilityState = editState,
                outsideClick = chooseNoteViewModel::cancelMenu,
                listOptionClick = chooseNoteViewModel::listArrangementSelected,
                gridOptionClick = chooseNoteViewModel::gridArrangementSelected,
                sortingSelected = chooseNoteViewModel::sortingSelected
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(backgroundColor = 0xFF000000)
@Composable
private fun TopBar(
    titleState: StateFlow<UserState<String>> = MutableStateFlow(UserState.ConnectedUser("Title")),
    accountClick: () -> Unit = {},
    menuClick: () -> Unit = {}
) {
    val title = titleState.collectAsState().value

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.clickable(onClick = accountClick),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val userIcon = Icons.Outlined.Person

                val fallbackPainter = rememberVectorPainter(
                    defaultWidth = userIcon.defaultWidth,
                    defaultHeight = userIcon.defaultHeight,
                    viewportWidth = userIcon.viewportWidth,
                    viewportHeight = userIcon.viewportHeight,
                    name = userIcon.name,
                    tintColor = MaterialTheme.colorScheme.onPrimary,
                    tintBlendMode = userIcon.tintBlendMode,
                    autoMirror = userIcon.autoMirror,
                ) { _, _ ->
                    RenderVectorGroup(group = userIcon.root)
                }

                AsyncImage(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .size(48.dp)
                        .padding(10.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("")
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    placeholder = fallbackPainter,
                    error = fallbackPainter
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    modifier = Modifier.let { modifierLet ->
                        if (title is UserState.Loading) {
                            modifierLet.shimmer()
                        } else {
                            modifierLet
                        }
                    },
                    text = getUserName(title),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = menuClick)
                    .padding(10.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
//                stringResource(R.string.more_options),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}

@Composable
private fun getUserName(userNameState: UserState<String>): String =
    when (userNameState) {
        is UserState.ConnectedUser ->  "${userNameState}\'s Workspace"
//            stringResource(id = R.string.name_space, userNameState.data)
        is UserState.DisconnectedUser -> "Offline Workspace"
//            stringResource(id = R.string.offline_workspace)
        is UserState.Idle -> ""
        is UserState.Loading -> ""
        is UserState.UserNotReturned -> "Disconnected"
//            stringResource(id = R.string.disconnected)
    }

@Composable
private fun FloatingActionButton(newNoteClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.semantics {
            testTag = ADD_NOTE_TEST_TAG
        },
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = newNoteClick,
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add note"
//                stringResource(R.string.add_note)
            )
        }
    )
}

@Composable
private fun Content(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    paddingValues: PaddingValues,
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Notes(
            chooseNoteViewModel = chooseNoteViewModel,
            navigateToNote = navigateToNote,
            selectionListener = selectionListener,
        )
    }
}

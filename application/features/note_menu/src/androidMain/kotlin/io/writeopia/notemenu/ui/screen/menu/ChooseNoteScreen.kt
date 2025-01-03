package io.writeopia.notemenu.ui.screen.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.valentinilk.shimmer.shimmer
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.notemenu.ui.screen.configuration.molecules.MobileConfigurationsMenu
import io.writeopia.notemenu.ui.screen.configuration.molecules.NotesSelectionMenu
import io.writeopia.notemenu.ui.screen.documents.ADD_NOTE_TEST_TAG
import io.writeopia.notemenu.ui.screen.documents.NotesCards
import io.writeopia.notemenu.viewmodel.ChooseNoteViewModel
import io.writeopia.notemenu.viewmodel.UserState
import io.writeopia.notemenu.viewmodel.toNumberDesktop
import io.writeopia.ui.draganddrop.target.DraggableScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun ChooseNoteScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    newNote: () -> Unit,
    navigateToAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(key1 = "refresh", block = {
        chooseNoteViewModel.requestUser()
//        chooseNoteViewModel.requestDocuments(false)
    })

    val hasSelectedNotes by chooseNoteViewModel.hasSelectedNotes.collectAsState()
    val editState by chooseNoteViewModel.editState.collectAsState()

    BackHandler(hasSelectedNotes || editState) {
        when {
            editState -> {
                chooseNoteViewModel.cancelEditMenu()
            }

            hasSelectedNotes -> {
                chooseNoteViewModel.clearSelection()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    titleState = chooseNoteViewModel.userName,
                    accountClick = navigateToAccount,
                    menuClick = chooseNoteViewModel::showEditMenu
                )
            },
            floatingActionButton = {
                FloatingActionButton(newNoteClick = newNote)
            }
        ) { paddingValues ->
            DraggableScreen {
                Content(
                    chooseNoteViewModel = chooseNoteViewModel,
                    loadNote = navigateToNote,
                    selectionListener = chooseNoteViewModel::onDocumentSelected,
                    paddingValues = paddingValues,
                )
            }
        }

        NotesSelectionMenu(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            visibilityState = hasSelectedNotes,
            onDelete = chooseNoteViewModel::deleteSelectedNotes,
            onCopy = chooseNoteViewModel::copySelectedNotes,
            onFavorite = chooseNoteViewModel::favoriteSelectedNotes,
            onClose = chooseNoteViewModel::unSelectNotes,
            shape = MaterialTheme.shapes.large
        )

        val selected = chooseNoteViewModel.notesArrangement.toNumberDesktop()

        MobileConfigurationsMenu(
            selected = selected,
            visibilityState = editState,
            outsideClick = chooseNoteViewModel::cancelEditMenu,
            staggeredGridOptionClick = chooseNoteViewModel::staggeredGridArrangementSelected,
            gridOptionClick = chooseNoteViewModel::gridArrangementSelected,
            listOptionClick = chooseNoteViewModel::listArrangementSelected,
            sortingSelected = chooseNoteViewModel::sortingSelected,
            sortingState = chooseNoteViewModel.orderByState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
// @Preview(backgroundColor = 0xFF000000)
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
                val userIcon = WrIcons.person

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
        is UserState.ConnectedUser ->
            "${userNameState.data.takeIf { it.isNotEmpty() } ?: "Unkown"}\'s Workspace"
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
                imageVector = WrIcons.add,
                contentDescription = "Add note"
//                stringResource(R.string.add_note)
            )
        }
    )
}

@Composable
private fun Content(
    chooseNoteViewModel: ChooseNoteViewModel,
    loadNote: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    paddingValues: PaddingValues,
) {
    NotesCards(
        documents = chooseNoteViewModel.documentsState.collectAsState().value,
        loadNote = loadNote,
        selectionListener = selectionListener,
        folderClick = {},
        changeIcon = { _, _, _, _ -> },
        moveRequest = { _, _ -> },
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    )
}

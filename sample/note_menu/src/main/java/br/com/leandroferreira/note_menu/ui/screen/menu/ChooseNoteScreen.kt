package br.com.leandroferreira.note_menu.ui.screen.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.note_menu.ui.screen.configuration.ConfigurationsMenu
import br.com.leandroferreira.note_menu.ui.screen.configuration.NotesSelectionMenu
import br.com.leandroferreira.note_menu.viewmodel.ChooseNoteViewModel
import br.com.leandroferreira.resourcers.R

const val DOCUMENT_ITEM_TEST_TAG = "DocumentItem_"
const val ADD_NOTE_TEST_TAG = "addNote"

@Composable
fun ChooseNoteScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    newNote: () -> Unit,
) {
    LaunchedEffect(key1 = "refresh", block = {
        chooseNoteViewModel.requestDocuments(false)
    })

    val hasSelectedNotes by chooseNoteViewModel.hasSelectedNotes.collectAsStateWithLifecycle()
    val editState by chooseNoteViewModel.editState.collectAsStateWithLifecycle()

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
                    TopBar(menuClick = chooseNoteViewModel::editMenu)
                },
                floatingActionButton = {
                    FloatingActionButton(newNoteClick = newNote)
                }
            ) { paddingValues ->
                Content(
                    chooseNoteViewModel = chooseNoteViewModel,
                    navigateToNote = navigateToNote,
                    selectionListener = chooseNoteViewModel::selectionListener,
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
@Composable
private fun TopBar(menuClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "StoryTeller",
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = menuClick)
                    .padding(10.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more_options),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
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
                contentDescription = stringResource(R.string.add_note)
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
            selectionListener = selectionListener
        )
    }
}

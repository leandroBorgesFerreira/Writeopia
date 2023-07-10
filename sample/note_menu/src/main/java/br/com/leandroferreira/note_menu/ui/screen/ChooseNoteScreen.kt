package br.com.leandroferreira.note_menu.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.resourcers.R
import br.com.leandroferreira.utils.ResultData
import br.com.leandroferreira.note_menu.ui.dto.DocumentCard
import br.com.leandroferreira.note_menu.viewmodel.ChooseNoteViewModel
import br.com.leandroferreira.note_menu.viewmodel.NotesArrangement
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryUnitDrawer
import com.github.leandroborgesferreira.storyteller.drawer.preview.CheckItemPreviewDrawer
import com.github.leandroborgesferreira.storyteller.drawer.preview.MessagePreviewDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.uicomponents.SwipeBox

private fun previewDrawers(): Map<String, StoryUnitDrawer> =
    mapOf(
        StoryType.MESSAGE.type to MessagePreviewDrawer(),
        StoryType.CHECK_ITEM.type to CheckItemPreviewDrawer()
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseNoteScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    newNote: () -> Unit
) {
    chooseNoteViewModel.requestDocuments()

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
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
                                    .clickable(onClick = chooseNoteViewModel::editMenu)
                                    .padding(10.dp),
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = stringResource(R.string.more_options),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.semantics {
                            testTag = "addNote"
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = newNote,
                        content = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_note)
                            )
                        }
                    )
                }
            ) { paddingValues ->
                Content(
                    chooseNoteViewModel = chooseNoteViewModel,
                    navigateToNote = navigateToNote,
                    paddingValues = paddingValues,
                )
            }

            val editState by chooseNoteViewModel.editState.collectAsStateWithLifecycle()

            ConfigurationsMenu(
                editState = editState,
                outsideClick = chooseNoteViewModel::cancelMenu,
                listOptionClick = chooseNoteViewModel::listArrangementSelected,
                gridOptionClick = chooseNoteViewModel::gridArrangementSelected,
                sortingSelected = chooseNoteViewModel::sortingSelected
            )
        }
    }
}


@Composable
private fun Content(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    paddingValues: PaddingValues,
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Notes(chooseNoteViewModel = chooseNoteViewModel, navigateToNote = navigateToNote)
    }
}

@Composable
private fun Notes(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit
) {
    when (val documents =
        chooseNoteViewModel.documentsState.collectAsStateWithLifecycle().value) {
        is ResultData.Complete -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                val data = documents.data

                if (data.isEmpty()) {
                    MockDataScreen(chooseNoteViewModel)
                } else {
                    val arrangement by chooseNoteViewModel.notesArrangement
                        .collectAsStateWithLifecycle()

                    when (arrangement) {
                        NotesArrangement.GRID -> {
                            LazyGridNotes(documents.data, navigateToNote)
                        }

                        NotesArrangement.LIST -> {
                            LazyColumnNotes(documents.data, navigateToNote)
                        }

                        else -> {
                            LazyGridNotes(documents.data, navigateToNote)
                        }
                    }
                }
            }
        }

        is ResultData.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = ""// stringResource(R.string.error_loading_notes)
                )
            }
        }

        is ResultData.Loading, is ResultData.Idle -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyGridNotes(
    documents: List<DocumentCard>,
    onDocumentClick: (String, String) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(6.dp),
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        content = {
            items(documents) { document ->
                DocumentItem(document, onDocumentClick, previewDrawers())
            }
        }
    )
}

@Composable
private fun LazyColumnNotes(
    documents: List<DocumentCard>,
    onDocumentClick: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        content = {
            items(documents) { document ->
                DocumentItem(document, onDocumentClick, previewDrawers())
            }
        }
    )
}

@Composable
private fun DocumentItem(
    documentCard: DocumentCard,
    documentClick: (String, String) -> Unit,
    drawers: Map<String, StoryUnitDrawer>,
) {
    var editState by remember {
        mutableStateOf(false)
    }

    SwipeBox(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .fillMaxWidth()
            .clickable {
                documentClick(documentCard.documentId, documentCard.title)
            },
        state = editState,
        swipeListener = { editState = !editState },
        cornersShape = MaterialTheme.shapes.large,
        defaultColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = documentCard.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            documentCard.preview.forEachIndexed { i, storyStep ->
                drawers[storyStep.type]?.Step(
                    step = storyStep, drawInfo =
                    DrawInfo(editable = false, position = i)
                )
            }
        }
    }
}


@Composable
private fun MockDataScreen(chooseNoteViewModel: ChooseNoteViewModel) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.you_dont_have_notes)
            )
            Button(onClick = {
                chooseNoteViewModel.addMockData(context)
            }) {
                Text(text = stringResource(R.string.add_sample_notes))
            }
        }
    }
}

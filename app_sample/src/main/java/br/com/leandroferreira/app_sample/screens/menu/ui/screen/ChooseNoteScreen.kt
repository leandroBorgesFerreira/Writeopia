package br.com.leandroferreira.app_sample.screens.menu.ui.screen

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.app_sample.screens.menu.ui.dto.DocumentCard
import br.com.leandroferreira.app_sample.screens.menu.viewmodel.ChooseNoteViewModel
import br.com.leandroferreira.app_sample.screens.menu.viewmodel.NotesArrangement
import br.com.leandroferreira.app_sample.utils.ResultData
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryUnitDrawer
import com.github.leandroborgesferreira.storyteller.drawer.preview.CheckItemPreviewDrawer
import com.github.leandroborgesferreira.storyteller.drawer.preview.MessagePreviewDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryType

private fun previewDrawers(): Map<String, StoryUnitDrawer> =
    mapOf(
        StoryType.MESSAGE.type to MessagePreviewDrawer(),
        StoryType.CHECK_ITEM.type to CheckItemPreviewDrawer()
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseNoteScreen(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String?) -> Unit
) {
    chooseNoteViewModel.requestDocuments()

    var configOptionsAppears by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "StoryTeller")
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                configOptionsAppears = !configOptionsAppears
                            },
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.more_options)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToNote(null) }, content = {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            })
        }
    ) { paddingValues ->
        Content(
            chooseNoteViewModel = chooseNoteViewModel,
            navigateToNote = navigateToNote,
            paddingValues = paddingValues,
            editState = configOptionsAppears
        )
    }
}


@Composable
private fun Content(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String) -> Unit,
    paddingValues: PaddingValues,
    editState: Boolean
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Notes(chooseNoteViewModel = chooseNoteViewModel, navigateToNote = navigateToNote)

        ConfigurationsMenu(
            editState = editState,
            listOptionClick = chooseNoteViewModel::listArrangementSelected,
            gridOptionClick = chooseNoteViewModel::gridArrangementSelected,
            sortingSelected = chooseNoteViewModel::sortingSelected
        )
    }
}

@Composable
private fun Notes(chooseNoteViewModel: ChooseNoteViewModel, navigateToNote: (String) -> Unit) {
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
                    }
                }
            }
        }

        is ResultData.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.error_loading_notes)
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
private fun LazyGridNotes(documents: List<DocumentCard>, onDocumentClick: (String) -> Unit) {
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
private fun LazyColumnNotes(documents: List<DocumentCard>, onDocumentClick: (String) -> Unit) {
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
    documentClick: (String) -> Unit,
    drawers: Map<String, StoryUnitDrawer>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
            .clickable {
                documentClick(documentCard.documentId)
            },
        shape = RoundedCornerShape(12.dp)
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

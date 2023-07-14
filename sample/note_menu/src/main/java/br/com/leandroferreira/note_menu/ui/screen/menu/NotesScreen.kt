package br.com.leandroferreira.note_menu.ui.screen.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leandroferreira.note_menu.ui.dto.DocumentUi
import br.com.leandroferreira.note_menu.viewmodel.ChooseNoteViewModel
import br.com.leandroferreira.note_menu.viewmodel.NotesArrangement
import br.com.leandroferreira.resourcers.R
import br.com.leandroferreira.utils.ResultData
import com.github.leandroborgesferreira.storyteller.drawer.DrawInfo
import com.github.leandroborgesferreira.storyteller.drawer.StoryUnitDrawer
import com.github.leandroborgesferreira.storyteller.drawer.preview.CheckItemPreviewDrawer
import com.github.leandroborgesferreira.storyteller.drawer.preview.MessagePreviewDrawer
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.uicomponents.SwipeBox


@Composable
internal fun Notes(
    chooseNoteViewModel: ChooseNoteViewModel,
    navigateToNote: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
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
                            LazyGridNotes(
                                documents.data,
                                selectionListener = selectionListener,
                                onDocumentClick = navigateToNote
                            )
                        }

                        NotesArrangement.LIST -> {
                            LazyColumnNotes(
                                documents.data,
                                selectionListener = selectionListener,
                                onDocumentClick = navigateToNote
                            )
                        }

                        else -> {
                            LazyGridNotes(
                                documents.data,
                                selectionListener = selectionListener,
                                onDocumentClick = navigateToNote
                            )
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
    documents: List<DocumentUi>,
    onDocumentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(6.dp),
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        content = {
            items(documents, key = { document -> document.hashCode() }) { document ->
                DocumentItem(document, onDocumentClick, selectionListener, previewDrawers())
            }
        }
    )
}

@Composable
private fun LazyColumnNotes(
    documents: List<DocumentUi>,
    onDocumentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        content = {
            items(documents, key = { document -> document.hashCode() }) { document ->
                DocumentItem(document, onDocumentClick, selectionListener, previewDrawers())
            }
        }
    )
}

@Composable
private fun DocumentItem(
    documentUi: DocumentUi,
    documentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    drawers: Map<String, StoryUnitDrawer>
) {
    SwipeBox(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .fillMaxWidth()
            .clickable {
                documentClick(documentUi.documentId, documentUi.title)
            }
            .semantics {
                testTag = "$DOCUMENT_ITEM_TEST_TAG${documentUi.title}"
            },
        state = documentUi.selected,
        swipeListener = { state -> selectionListener(documentUi.documentId, state) },
        cornersShape = MaterialTheme.shapes.large,
        defaultColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = documentUi.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            documentUi.preview.forEachIndexed { i, storyStep ->
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

private fun previewDrawers(): Map<String, StoryUnitDrawer> =
    mapOf(
        StoryType.MESSAGE.type to MessagePreviewDrawer(),
        StoryType.CHECK_ITEM.type to CheckItemPreviewDrawer()
    )
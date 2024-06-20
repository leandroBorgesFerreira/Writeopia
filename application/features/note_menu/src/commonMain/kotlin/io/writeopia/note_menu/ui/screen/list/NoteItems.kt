package io.writeopia.note_menu.ui.screen.list

//import io.writeopia.appresourcers.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.data.model.NotesArrangement
import io.writeopia.note_menu.ui.dto.DocumentUi
import io.writeopia.note_menu.ui.dto.NotesUi
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.components.SwipeBox
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.drawer.preview.CheckItemPreviewDrawer
import io.writeopia.ui.drawer.preview.HeaderPreviewDrawer
import io.writeopia.ui.drawer.preview.TextPreviewDrawer
import io.writeopia.ui.drawer.preview.UnOrderedListItemPreviewDrawer
import io.writeopia.utils_module.ResultData
import org.jetbrains.compose.ui.tooling.preview.Preview

const val DOCUMENT_ITEM_TEST_TAG = "DocumentItem_"
const val ADD_NOTE_TEST_TAG = "addNote"

@Composable
fun NotesCards(
    documents: ResultData<NotesUi>,
    loadNote: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (documents) {
        is ResultData.Complete -> {
            Column(modifier = modifier) {
                val notesUi: NotesUi = documents.data

                if (notesUi.documentUiList.isEmpty()) {
                    NoNotesScreen()
                } else {
                    val documentsUiList = notesUi.documentUiList

                    when (notesUi.notesArrangement) {
                        NotesArrangement.STAGGERED_GRID -> {
                            LazyStaggeredGridNotes(
                                documentsUiList,
                                selectionListener = selectionListener,
                                onDocumentClick = loadNote
                            )
                        }

                        NotesArrangement.GRID -> {
                            LazyGridNotes(
                                documentsUiList,
                                selectionListener = selectionListener,
                                onDocumentClick = loadNote
                            )
                        }

                        NotesArrangement.LIST -> {
                            LazyColumnNotes(
                                documentsUiList,
                                selectionListener = selectionListener,
                                onDocumentClick = loadNote
                            )
                        }
                    }
                }
            }
        }

        is ResultData.Error -> {
            LaunchedEffect(key1 = true) {
                documents.exception.printStackTrace()
            }

            Box(modifier = modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Error!!"// stringResource(R.string.error_loading_notes)
                )
            }
        }

        is ResultData.Loading, is ResultData.Idle -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyStaggeredGridNotes(
    documents: List<DocumentUi>,
    onDocumentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(6.dp),
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        content = {
            items(
                documents,
                key = { document -> document.hashCode() }
            ) { document ->
                DocumentItem(
                    document,
                    onDocumentClick,
                    selectionListener,
                    previewDrawers(),
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyGridNotes(
    documents: List<DocumentUi>,
    onDocumentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(6.dp),
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        content = {
            items(
                documents,
                key = { document -> document.hashCode() }
            ) { document ->
                DocumentItem(
                    document,
                    onDocumentClick,
                    selectionListener,
                    previewDrawers(),
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
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
                DocumentItem(
                    document,
                    onDocumentClick,
                    selectionListener,
                    previewDrawers(),
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    )
}

@Composable
private fun DocumentItem(
    documentUi: DocumentUi,
    documentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    drawers: Map<Int, StoryStepDrawer>,
    modifier: Modifier = Modifier
) {
    val titleFallback = "untitled"
//        stringResource(R.string.untitled)

    SwipeBox(
        modifier = modifier
            .padding(bottom = 6.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable {
                documentClick(
                    documentUi.documentId,
                    documentUi.title.takeIf { it.isNotEmpty() } ?: titleFallback
                )
            }
            .semantics {
                testTag = "$DOCUMENT_ITEM_TEST_TAG${documentUi.title}"
            },
        isOnEditState = documentUi.selected,
        swipeListener = { state ->
            selectionListener(documentUi.documentId, state)
        },
        cornersShape = MaterialTheme.shapes.large,
        defaultColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column {
            documentUi.preview.forEachIndexed { i, storyStep ->
                drawers[storyStep.type.number]?.Step(
                    step = storyStep,
                    drawInfo = DrawInfo(editable = false, position = i)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (documentUi.isFavorite) {
            Icon(
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "Favorite",
                tint = Color.Red
            )
        }
    }
}

@Preview
@Composable
fun DocumentItemPreview() {
    val documentUi = DocumentUi(
        documentId = "documentId",
        title = "title",
        lastEdit = "lastEdit",
        preview = listOf(
            StoryStep(type = StoryTypes.TITLE.type, text = "Title"),
            StoryStep(type = StoryTypes.TEXT.type, text = "some text"),
            StoryStep(type = StoryTypes.CHECK_ITEM.type, text = "some text"),
            StoryStep(type = StoryTypes.UNORDERED_LIST_ITEM.type, text = "some text")
        ),
        selected = false,
        isFavorite = true
    )

    DocumentItem(
        documentUi = documentUi,
        documentClick = { _, _ -> },
        selectionListener = { _, _ -> },
        drawers = previewDrawers(),
        modifier = Modifier.padding(10.dp)
    )
}


@Preview
@Composable
fun DocumentItemSelectedPreview() {
    val documentUi = DocumentUi(
        documentId = "documentId",
        title = "title",
        lastEdit = "lastEdit",
        preview = listOf(
            StoryStep(type = StoryTypes.TITLE.type, text = "Title"),
            StoryStep(type = StoryTypes.TEXT.type, text = "some text"),
            StoryStep(type = StoryTypes.CHECK_ITEM.type, text = "some text"),
            StoryStep(type = StoryTypes.UNORDERED_LIST_ITEM.type, text = "some text")
        ),
        selected = true,
        isFavorite = true
    )

    DocumentItem(
        documentUi = documentUi,
        documentClick = { _, _ -> },
        selectionListener = { _, _ -> },
        drawers = previewDrawers(),
        modifier = Modifier.padding(10.dp)
    )
}


@Preview
@Composable
private fun NoNotesScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            text = "You don\'t have notes",
//            stringResource(R.string.you_dont_have_notes),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun previewDrawers(): Map<Int, StoryStepDrawer> {
    val unOrderedListItemPreviewDrawer = UnOrderedListItemPreviewDrawer()

    return mapOf(
        StoryTypes.TITLE.type.number to HeaderPreviewDrawer(
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            )
        ),
        StoryTypes.CHECK_ITEM.type.number to CheckItemPreviewDrawer(),
        StoryTypes.TEXT.type.number to TextPreviewDrawer(),
        StoryTypes.UNORDERED_LIST_ITEM.type.number to unOrderedListItemPreviewDrawer,
    )
}

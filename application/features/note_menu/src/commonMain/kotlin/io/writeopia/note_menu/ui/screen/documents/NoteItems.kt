package io.writeopia.note_menu.ui.screen.documents

//import io.writeopia.appresourcers.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Folder
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.data.model.NotesArrangement
import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.note_menu.ui.dto.NotesUi
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.components.SwipeBox
import io.writeopia.ui.draganddrop.target.DragCardTargetWithDragItem
import io.writeopia.ui.draganddrop.target.DropTarget
import io.writeopia.ui.draganddrop.target.SizeDp
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
    folderClick: (String) -> Unit,
    moveRequest: (MenuItemUi, String) -> Unit,
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
                                onDocumentClick = loadNote,
                                folderClick = folderClick,
                                moveRequest = moveRequest
                            )
                        }

                        NotesArrangement.GRID -> {
                            LazyGridNotes(
                                documentsUiList,
                                selectionListener = selectionListener,
                                onDocumentClick = loadNote,
                                folderClick = folderClick,
                                moveRequest = moveRequest
                            )
                        }

                        NotesArrangement.LIST -> {
                            LazyColumnNotes(
                                documentsUiList,
                                selectionListener = selectionListener,
                                onDocumentClick = loadNote,
                                folderClick = folderClick,
                                moveRequest = moveRequest
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
    documents: List<MenuItemUi>,
    onDocumentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    moveRequest: (MenuItemUi, String) -> Unit,
    folderClick: (String) -> Unit,
) {
    val spacing = 6.dp

    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(6.dp),
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalItemSpacing = spacing,
        content = {
            itemsIndexed(
                documents,
                key = { _, menuItem -> menuItem.hashCode() }
            ) { i, menuItem ->
                val modifier = Modifier.animateItemPlacement()

                when (menuItem) {
                    is MenuItemUi.DocumentUi -> {
                        DocumentItem(
                            menuItem,
                            onDocumentClick,
                            selectionListener,
                            previewDrawers(),
                            position = i,
                            modifier = modifier,
                        )
                    }

                    is MenuItemUi.FolderUi -> {
                        FolderItem(
                            menuItem,
                            folderClick = folderClick,
                            position = i,
                            moveRequest = moveRequest,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyGridNotes(
    documents: List<MenuItemUi>,
    onDocumentClick: (String, String) -> Unit,
    folderClick: (String) -> Unit,
    moveRequest: (MenuItemUi, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
) {
    val spacing = Arrangement.spacedBy(6.dp)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = spacing,
        verticalArrangement = spacing,
        content = {
            itemsIndexed(
                documents,
                key = { _, menuItem -> menuItem.hashCode() }
            ) { i, menuItem ->
                when (menuItem) {
                    is MenuItemUi.DocumentUi -> {
                        DocumentItem(
                            menuItem,
                            onDocumentClick,
                            selectionListener,
                            previewDrawers(),
                            position = i,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }

                    is MenuItemUi.FolderUi -> {
                        FolderItem(
                            menuItem,
                            folderClick = folderClick,
                            moveRequest = moveRequest,
                            position = i
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyColumnNotes(
    documents: List<MenuItemUi>,
    onDocumentClick: (String, String) -> Unit,
    folderClick: (String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    moveRequest: (MenuItemUi, String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        content = {
            itemsIndexed(
                documents,
                key = { _, menuItem -> menuItem.hashCode() }
            ) { i, menuItem ->
                when (menuItem) {
                    is MenuItemUi.DocumentUi -> {
                        DocumentItem(
                            menuItem,
                            onDocumentClick,
                            selectionListener,
                            previewDrawers(),
                            position = i,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }

                    is MenuItemUi.FolderUi -> {
                        FolderItem(
                            menuItem,
                            folderClick,
                            moveRequest = moveRequest,
                            position = i
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun FolderItem(
    folderUi: MenuItemUi.FolderUi,
    folderClick: (String) -> Unit,
    position: Int,
    moveRequest: (MenuItemUi, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropTarget { inBound, data ->
        val menuItemUI = data?.info as? MenuItemUi
        if (inBound && menuItemUI != null) {
            LaunchedEffect(menuItemUI) {
                moveRequest(menuItemUI, folderUi.documentId)
            }
        }

        val bgColor =
            when {
                inBound -> Color.LightGray
//                    BuildConfig.DEBUG -> Color.Cyan
                else -> MaterialTheme.colorScheme.surfaceVariant
            }

        DragCardTargetWithDragItem(
            modifier = modifier
                .clickable {
                    folderClick(folderUi.documentId)
                },
            position = position,
            dataToDrop = DropInfo(folderUi, position),
            limitSize = SizeDp(150.dp, 150.dp)

        ) {
            Column(
                modifier = Modifier
                    .background(color = bgColor, shape = MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .padding(bottom = 26.dp, top = 10.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(70.dp).padding(12.dp),
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = "Folder",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = folderUi.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${folderUi.itemsCount} items",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun DocumentItem(
    documentUi: MenuItemUi.DocumentUi,
    documentClick: (String, String) -> Unit,
    selectionListener: (String, Boolean) -> Unit,
    drawers: Map<Int, StoryStepDrawer>,
    position: Int,
    modifier: Modifier = Modifier
) {
    val titleFallback = "untitled"
//        stringResource(R.string.untitled)

    val backgroundColor = if (documentUi.selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    SwipeBox(
        modifier = modifier
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
        DragCardTargetWithDragItem(
            position = position,
            dataToDrop = DropInfo(documentUi, position),
            limitSize = SizeDp(150.dp, 120.dp)
        ) {
            Column(
                modifier = Modifier.background(
                    color = backgroundColor,
                    shape = MaterialTheme.shapes.large
                )
            ) {
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
                    modifier = Modifier.align(Alignment.TopEnd).size(40.dp).padding(12.dp),
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun DocumentItemPreview() {
    val menuItem = MenuItemUi.DocumentUi(
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
        isFavorite = true,
        parentId = "",
        highlighted = false,
    )

    DocumentItem(
        documentUi = menuItem,
        documentClick = { _, _ -> },
        selectionListener = { _, _ -> },
        drawers = previewDrawers(),
        position = 0,
        modifier = Modifier.padding(10.dp)
    )
}


@Preview
@Composable
fun DocumentItemSelectedPreview() {
    val menuItem = MenuItemUi.DocumentUi(
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
        isFavorite = true,
        parentId = "",
        highlighted = false,
    )

    DocumentItem(
        documentUi = menuItem,
        documentClick = { _, _ -> },
        selectionListener = { _, _ -> },
        drawers = previewDrawers(),
        position = 0,
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

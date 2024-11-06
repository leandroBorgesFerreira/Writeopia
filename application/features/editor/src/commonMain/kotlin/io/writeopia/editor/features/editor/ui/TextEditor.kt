package io.writeopia.editor.features.editor.ui

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.writeopia.editor.features.editor.viewmodel.NoteEditorViewModel
import io.writeopia.ui.WriteopiaEditor
import io.writeopia.ui.drawer.factory.DrawersFactory
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun TextEditor(
    noteEditorViewModel: NoteEditorViewModel,
    drawersFactory: DrawersFactory,
    modifier: Modifier = Modifier,
) {
    val storyState by noteEditorViewModel.toDraw.collectAsState()
    val editable by noteEditorViewModel.isEditable.collectAsState()
    val listState: LazyListState = rememberLazyListState()
    val position by noteEditorViewModel.scrollToPosition.collectAsState()

    if (position != null) {
        LaunchedEffect(position, block = {
            noteEditorViewModel.scrollToPosition.collectLatest {
                listState.animateScrollBy(70F)
            }
        })
    }

    val clipShape = MaterialTheme.shapes.medium

    WriteopiaEditor(
        modifier = modifier,
        editable = editable,
        listState = listState,
        drawers = drawersFactory.create(
            noteEditorViewModel.writeopiaManager,
            defaultBorder = clipShape,
            onHeaderClick = noteEditorViewModel::onHeaderClick,
            editable = true,
            groupsBackgroundColor = Color.Transparent,
        ),
        storyState = storyState,
    )
}

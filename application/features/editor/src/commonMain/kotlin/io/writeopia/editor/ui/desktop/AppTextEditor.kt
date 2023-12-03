package io.writeopia.editor.ui.desktop

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.writeopia.editor.ui.TextEditor
import io.writeopia.editor.viewmodel.NoteEditorKmpViewModel
import io.writeopia.sdk.drawer.factory.DrawersFactory
import io.writeopia.sdk.manager.WriteopiaManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AppTextEditor(
    manager: WriteopiaManager,
    viewModel: NoteEditorKmpViewModel,
    drawersFactory: DrawersFactory,
    loadNoteId: String? = null
) {
    val listState: LazyListState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    coroutine.launch {
        manager.scrollToPosition.collectLatest {
            listState.animateScrollBy(70F)
        }
    }

    viewModel.initCoroutine(coroutine)
    if (loadNoteId == null) {
        viewModel.createNewDocument(UUID.randomUUID().toString(), "")
    } else {
        viewModel.loadDocument(loadNoteId)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextEditor(viewModel, drawersFactory)
    }
}
package io.writeopia.editor.ui.desktop

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import io.writeopia.editor.ui.TextEditor
import io.writeopia.editor.viewmodel.NoteEditorKmpViewModel
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.drawer.factory.DrawersFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AppTextEditor(
    manager: WriteopiaStateManager,
    viewModel: NoteEditorKmpViewModel,
    drawersFactory: DrawersFactory,
    loadNoteId: String? = null,
    modifier: Modifier = Modifier
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
        viewModel.createNewDocument(GenerateId.generate(), "")
    } else {
        viewModel.loadDocument(loadNoteId)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        TextEditor(viewModel, drawersFactory)
    }
}
package io.writeopia.editor.ui.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import io.writeopia.editor.viewmodel.NoteEditorViewModel
import io.writeopia.ui.drawer.factory.DrawersFactory

@Composable
fun DesktopNoteEditorScreen(
    documentId: String?,
    noteEditorViewModel: NoteEditorViewModel,
    drawersFactory: DrawersFactory,
    navigateBack: () -> Unit,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    modifier: Modifier = Modifier,
) {
    EditorScaffold(
        clickAtBottom = noteEditorViewModel.writeopiaManager::clickAtTheEnd,
        onBackClick = navigateBack,
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        content = {
            AppTextEditor(
                noteEditorViewModel.writeopiaManager,
                noteEditorViewModel,
                drawersFactory = drawersFactory,
                loadNoteId = documentId,
                modifier = Modifier
                    .onPreviewKeyEvent { keyEvent ->
                        if (isUndoKeyEvent(keyEvent)) {
                            noteEditorViewModel.undo()
                            true
                        } else {
                            false
                        }
                    }.padding(horizontal = 30.dp)
            )
        }
    )
}

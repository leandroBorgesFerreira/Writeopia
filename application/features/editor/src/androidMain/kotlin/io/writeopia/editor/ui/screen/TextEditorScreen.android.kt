package io.writeopia.editor.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import io.writeopia.editor.viewmodel.NoteEditorViewModel

@Composable
actual fun TextEditorScreen(
    documentId: String?,
    title: String?,
    noteEditorViewModel: NoteEditorViewModel,
    navigateBack: () -> Unit,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    modifier: Modifier
) {
    NoteEditorScreen(
        documentId = documentId,
        title = title,
        noteEditorViewModel = noteEditorViewModel,
        navigateBack = navigateBack,
        modifier = modifier
    )
}

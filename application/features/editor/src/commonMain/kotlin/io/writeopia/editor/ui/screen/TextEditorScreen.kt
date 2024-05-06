package io.writeopia.editor.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import io.writeopia.editor.viewmodel.NoteEditorViewModel

@Composable
expect fun TextEditorScreen(
    documentId: String?,
    title: String?,
    noteEditorViewModel: NoteEditorViewModel,
    navigateBack: () -> Unit,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    modifier: Modifier = Modifier,
)

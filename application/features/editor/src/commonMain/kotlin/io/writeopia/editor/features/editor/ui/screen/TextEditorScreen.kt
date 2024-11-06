package io.writeopia.editor.features.editor.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import io.writeopia.editor.features.editor.viewmodel.NoteEditorViewModel

@Composable
expect fun TextEditorScreen(
    documentId: String?,
    title: String?,
    noteEditorViewModel: NoteEditorViewModel,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    navigateBack: () -> Unit,
    playPresentation: () -> Unit,
    modifier: Modifier = Modifier,
)

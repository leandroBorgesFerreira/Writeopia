package io.writeopia.editor.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.type
import io.writeopia.editor.ui.desktop.DesktopNoteEditorScreen
import io.writeopia.editor.viewmodel.NoteEditorViewModel
import io.writeopia.ui.drawer.factory.DefaultDrawersDesktop

@Composable
actual fun TextEditorScreen(
    documentId: String?,
    title: String?,
    noteEditorViewModel: NoteEditorViewModel,
    navigateBack: () -> Unit,
    isUndoKeyEvent: (KeyEvent) -> Boolean,
    modifier: Modifier,
) {
    DesktopNoteEditorScreen(
        documentId = documentId,
        noteEditorViewModel = noteEditorViewModel,
        drawersFactory = DefaultDrawersDesktop,
        navigateBack = navigateBack,
        isUndoKeyEvent = ::isUndoKeyboardEvent,
        modifier = modifier
    )
}

private fun isUndoKeyboardEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_Z &&
        keyEvent.type == KeyEventType.KeyDown

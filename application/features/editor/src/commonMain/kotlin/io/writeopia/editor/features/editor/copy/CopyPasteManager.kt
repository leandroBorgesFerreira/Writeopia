package io.writeopia.editor.features.editor.copy

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

class CopyPasteManager(private val clipboardManager: ClipboardManager) {

    fun copy(text: AnnotatedString) {
        clipboardManager.setText(text)
    }
}

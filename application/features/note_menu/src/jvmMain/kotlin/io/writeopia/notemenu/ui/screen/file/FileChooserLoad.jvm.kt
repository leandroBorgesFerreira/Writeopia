package io.writeopia.notemenu.ui.screen.file

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

actual fun fileChooserLoad(title: String): List<String> {
    val dialog = FileDialog(ComposeWindow(), title, FileDialog.LOAD).apply {
        isMultipleMode = true
        isVisible = true
    }

    return dialog.files?.takeIf { it.isNotEmpty() }?.map { it.absolutePath } ?: emptyList()
}

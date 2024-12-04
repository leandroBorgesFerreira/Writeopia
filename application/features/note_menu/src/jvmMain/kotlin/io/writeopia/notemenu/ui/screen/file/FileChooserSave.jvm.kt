package io.writeopia.notemenu.ui.screen.file

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

actual fun fileChooserSave(title: String): String? {
    val dialog = FileDialog(ComposeWindow(), title, FileDialog.SAVE).apply {
        isMultipleMode = true
        isVisible = true
    }

    return dialog.directory?.takeIf { it.isNotEmpty() }
}

package io.writeopia.common.utils.file

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

actual fun directoryChooserSave(title: String): String? {
    val dialog = FileDialog(ComposeWindow(), title, FileDialog.SAVE).apply {
        isMultipleMode = false
        isVisible = true
    }

    return dialog.directory?.takeIf { it.isNotEmpty() }
}

actual fun fileChooserSave(title: String): String? {
    val dialog = FileDialog(ComposeWindow(), title, FileDialog.SAVE).apply {
        isMultipleMode = false
        isVisible = true
    }

    return (dialog.directory + dialog.file).takeIf { it.isNotEmpty() }
}

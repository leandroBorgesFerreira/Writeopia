package io.writeopia.common.utils.file

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

actual fun fileChooserLoad(title: String): String? {
    val dialog = FileDialog(ComposeWindow(), title, FileDialog.LOAD).apply {
        isMultipleMode = false
        isVisible = true
    }

    return (dialog.directory + dialog.file).takeIf { it.isNotEmpty() }
}

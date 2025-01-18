package io.writeopia.common.utils.file

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

actual fun fileChooserLoad(title: String): String? {
    val dialog = FileDialog(ComposeWindow(), title, FileDialog.LOAD).apply {
        isMultipleMode = true
        isVisible = true
    }

    return dialog.directory?.takeIf { it.isNotEmpty() }
}

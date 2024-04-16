package io.writeopia.note_menu.ui.screen.file

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

actual fun fileChooser(title: String) {
    val dialog = FileDialog(ComposeWindow(), title, FileDialog.SAVE).apply {
        isMultipleMode = true
    }

    dialog.isVisible = true
    val file = dialog.directory
    println("File: $file")

}
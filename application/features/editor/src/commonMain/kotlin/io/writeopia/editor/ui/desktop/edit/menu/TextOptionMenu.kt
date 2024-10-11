package io.writeopia.editor.ui.desktop.edit.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextOptionsMenu() {
    DropdownMenu(expanded = true, onDismissRequest = { }) {
        Text("Column")

        Icon(imageVector = Icons.Default.FormatBold, contentDescription = "Bold text")
    }
}

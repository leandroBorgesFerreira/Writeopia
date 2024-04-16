package io.writeopia.note_menu.ui.screen.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DesktopNoteActionsMenu(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.FileUpload, contentDescription = "Export")
        }

        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.FileDownload, contentDescription = "Export")
        }
    }
}
package io.writeopia.note_menu.ui.screen.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.writeopia.note_menu.ui.screen.file.fileChooserLoad
import io.writeopia.note_menu.ui.screen.file.fileChooserSave

@Composable
fun DesktopNoteActionsMenu(
    modifier: Modifier = Modifier,
    showExtraOptions: Boolean,
    showExtraOptionsRequest: () -> Unit,
    hideExtraOptionsRequest: () -> Unit,
    exportAsMarkdownClick: () -> Unit,
    exportAsJsonClick: () -> Unit,
    importClick: () -> Unit
) {
    Row(modifier = modifier) {
        Box {
            IconButton(onClick = showExtraOptionsRequest) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Export")
            }

            DropdownMenu(expanded = showExtraOptions, onDismissRequest = hideExtraOptionsRequest) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Export"
                        )
                    }, onClick = exportAsMarkdownClick,
                    text = {
                        Text("Export as Markdown")
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Export"
                        )
                    }, onClick = exportAsJsonClick,
                    text = {
                        Text("Export as Json")
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Export"
                        )
                    },
                    onClick = importClick,
                    text = {
                        Text("Import file")
                    }
                )
            }
        }
    }
}


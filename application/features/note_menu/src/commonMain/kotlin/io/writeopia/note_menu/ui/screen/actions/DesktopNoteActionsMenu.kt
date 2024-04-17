package io.writeopia.note_menu.ui.screen.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.writeopia.note_menu.ui.screen.file.fileChooser

@Composable
fun DesktopNoteActionsMenu(
    modifier: Modifier = Modifier,
    showExtraOptions: Boolean,
    showExtraOptionsRequest: () -> Unit,
    hideExtraOptionsRequest: () -> Unit,
    fileChooserFun: (String) -> String? = ::fileChooser,
    exportAsMarkdownClick: (String) -> Unit,
    exportAsJsonClick: (String) -> Unit,
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
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = "Export"
                        )
                    }, onClick = {
                        fileChooserFun("")?.let(exportAsMarkdownClick)
                    },
                    text = {
                        Text("Export as Markdown")
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = "Export"
                        )
                    }, onClick = {
                        fileChooserFun("")?.let(exportAsJsonClick)
                    },
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
                    }, onClick = {
                        println("File chosen: ${fileChooserFun("")}")
                    },
                    text = {
                        Text("Import as JSON")
                    }
                )
            }
        }
    }
}
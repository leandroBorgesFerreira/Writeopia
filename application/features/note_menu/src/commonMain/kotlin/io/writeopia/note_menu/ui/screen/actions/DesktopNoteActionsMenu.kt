package io.writeopia.note_menu.ui.screen.actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.viewmodel.SyncState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DesktopNoteActionsMenu(
    modifier: Modifier = Modifier,
    showExtraOptions: Boolean,
    showExtraOptionsRequest: () -> Unit,
    hideExtraOptionsRequest: () -> Unit,
    configureDirectory: () -> Unit,
    exportAsMarkdownClick: () -> Unit,
    importClick: () -> Unit,
    syncInProgressState: StateFlow<SyncState>,
    onSyncLocallySelected: () -> Unit,
    onWriteLocallySelected: () -> Unit,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        val showSyncLoading by syncInProgressState.collectAsState()

        LoadingBox(showSyncLoading == SyncState.LoadingWrite) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save",
                modifier = Modifier.icon(onWriteLocallySelected)
                    .testTag("writeWorkspaceLocally")
            )
        }

        LoadingBox(showSyncLoading == SyncState.LoadingSync) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Save",
                modifier = Modifier.icon(onSyncLocallySelected)
                    .testTag("syncWorkspaceLocally")
            )
        }

        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Export",
                modifier = Modifier.icon(showExtraOptionsRequest)
            )

            DropdownMenu(expanded = showExtraOptions, onDismissRequest = hideExtraOptionsRequest) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Configure directory"
                        )
                    }, onClick = configureDirectory,
                    text = {
                        Text("Configure directory")
                    }
                )

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

@Composable
private fun LoadingBox(showLoading: Boolean, content: @Composable () -> Unit) {
    Box(modifier = Modifier.size(38.dp)) {
        if (showLoading) {
            CircularProgressIndicator(
                modifier = Modifier.then(Modifier.size(34.dp).padding(8.dp))
                    .align(Alignment.Center),
                strokeWidth = 2.dp
            )
        } else {
            content()
        }
    }
}

private fun Modifier.icon(onClick: () -> Unit): Modifier =
    this.clip(CircleShape)
        .clickable(onClick = onClick)
        .size(36.dp)
        .padding(6.dp)


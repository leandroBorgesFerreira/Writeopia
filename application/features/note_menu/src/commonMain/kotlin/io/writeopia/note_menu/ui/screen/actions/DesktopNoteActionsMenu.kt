package io.writeopia.note_menu.ui.screen.actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DesktopNoteActionsMenu(
    modifier: Modifier = Modifier,
    showSortingOption: Boolean,
    showSortOptionsRequest: () -> Unit,
    hideSortOptionsRequest: () -> Unit,
    selectSortOption: (OrderBy) -> Unit,
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
                    .testTag("writeWorkspaceLocally"),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        LoadingBox(showSyncLoading == SyncState.LoadingSync) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Save",
                modifier = Modifier.icon(onSyncLocallySelected)
                    .testTag("syncWorkspaceLocally"),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Box {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Sort,
                contentDescription = "Sort Options",
                modifier = Modifier.icon(showSortOptionsRequest),
                tint = MaterialTheme.colorScheme.onBackground
            )

            DropdownMenu(expanded = showSortingOption, onDismissRequest = hideSortOptionsRequest) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Sort by name"
                        )
                    }, onClick = {
                        selectSortOption(OrderBy.NAME)
                    },
                    text = {
                        Text("Sort by name")
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Sort by creation"
                        )
                    }, onClick = {
                        selectSortOption(OrderBy.CREATE)
                    },
                    text = {
                        Text("Sort by creation")
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Sort by last update"
                        )
                    }, onClick = {
                        selectSortOption(OrderBy.UPDATE)
                    },
                    text = {
                        Text("Sort by last update")
                    }
                )
            }
        }

        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Export",
                modifier = Modifier.icon(showExtraOptionsRequest),
                tint = MaterialTheme.colorScheme.onBackground
            )

            DropdownMenu(expanded = showExtraOptions, onDismissRequest = hideExtraOptionsRequest) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "Configure directory",
                            tint = MaterialTheme.colorScheme.onBackground
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
                            contentDescription = "Export",
                            tint = MaterialTheme.colorScheme.onBackground
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
                            contentDescription = "Export",
                            tint = MaterialTheme.colorScheme.onBackground
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


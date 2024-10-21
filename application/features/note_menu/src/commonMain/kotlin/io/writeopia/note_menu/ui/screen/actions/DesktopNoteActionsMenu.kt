package io.writeopia.note_menu.ui.screen.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.ui.screen.configuration.modifier.icon
import io.writeopia.note_menu.viewmodel.SyncState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DesktopNoteActionsMenu(
    modifier: Modifier = Modifier,
    showExtraOptions: StateFlow<Boolean>,
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
                imageVector = Icons.Outlined.Save,
                contentDescription = "Save",
                modifier = Modifier.icon(onWriteLocallySelected)
                    .testTag("writeWorkspaceLocally"),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        LoadingBox(showSyncLoading == SyncState.LoadingSync) {
            Icon(
                imageVector = Icons.Outlined.Sync,
                contentDescription = "Save",
                modifier = Modifier.icon(onSyncLocallySelected)
                    .testTag("syncWorkspaceLocally"),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        MoreOptions(
            showExtraOptions,
            showExtraOptionsRequest,
            hideExtraOptionsRequest,
            configureDirectory,
            exportAsMarkdownClick,
            importClick
        )
    }
}

@Composable
private fun MoreOptions(
    showExtraOptions: StateFlow<Boolean>,
    showExtraOptionsRequest: () -> Unit,
    hideExtraOptionsRequest: () -> Unit,
    configureDirectory: () -> Unit,
    exportAsMarkdownClick: () -> Unit,
    importClick: () -> Unit,
) {
    Box {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "Export",
            modifier = Modifier.icon(showExtraOptionsRequest),
            tint = MaterialTheme.colorScheme.onBackground
        )

        val showExtra by showExtraOptions.collectAsState()

        DropdownMenu(expanded = showExtra, onDismissRequest = hideExtraOptionsRequest) {
            val iconTintColor = MaterialTheme.colorScheme.onPrimary

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Folder,
                        contentDescription = "Configure directory",
                        tint = iconTintColor
                    )
                },
                onClick = configureDirectory,
                text = {
                    Text(
                        "Configure directory",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AttachFile,
                        contentDescription = "Export",
                        tint = iconTintColor
                    )
                },
                onClick = exportAsMarkdownClick,
                text = {
                    Text("Export as Markdown", color = MaterialTheme.colorScheme.onPrimary)
                }
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.FileDownload,
                        contentDescription = "Export",
                        tint = iconTintColor
                    )
                },
                onClick = importClick,
                text = {
                    Text("Import file", color = MaterialTheme.colorScheme.onPrimary)
                }
            )
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

@Preview
@Composable
private fun DesktopNoteActionsMenuPreview() {
    DesktopNoteActionsMenu(
        modifier = Modifier,
        showExtraOptions = MutableStateFlow(true),
        showExtraOptionsRequest = {},
        hideExtraOptionsRequest = {},
        configureDirectory = {},
        exportAsMarkdownClick = {},
        importClick = {},
        syncInProgressState = MutableStateFlow(SyncState.Idle),
        onSyncLocallySelected = {},
        onWriteLocallySelected = {},
    )
}

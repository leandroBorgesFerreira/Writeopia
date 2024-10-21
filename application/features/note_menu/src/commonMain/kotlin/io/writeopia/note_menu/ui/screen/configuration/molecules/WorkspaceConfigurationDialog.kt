package io.writeopia.note_menu.ui.screen.configuration.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.writeopia.note_menu.ui.screen.file.fileChooserSave
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WorkspaceConfigurationDialog(
    currentPath: String?,
    pathChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(30.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = "You need to configure the path of this workspace to sync it",
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = currentPath ?: "",
                            onValueChange = pathChange,
                            modifier = Modifier.width(400.dp),
                            textStyle = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Outlined.Folder,
                            "Choose directory",
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    fileChooserSave("")?.let(pathChange)
                                }
                                .padding(8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Dismiss")
                        }

                        TextButton(
                            onClick = onConfirmation,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun WorkspaceConfigurationDialogPreview() {
    Box(modifier = Modifier.size(500.dp)) {
        WorkspaceConfigurationDialog(
            currentPath = "current/path",
            pathChange = {},
            onDismissRequest = {},
            onConfirmation = {},
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

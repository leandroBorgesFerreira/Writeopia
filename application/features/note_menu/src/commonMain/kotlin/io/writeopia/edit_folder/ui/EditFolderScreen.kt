package io.writeopia.edit_folder.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateFolderScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center).width(400.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Folder name")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = {}) {
                Text("Create")
            }
        }
    }
}

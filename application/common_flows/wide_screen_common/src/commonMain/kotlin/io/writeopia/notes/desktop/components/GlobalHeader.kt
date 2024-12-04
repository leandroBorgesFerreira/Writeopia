package io.writeopia.notes.desktop.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.notemenu.ui.screen.configuration.modifier.icon
import kotlinx.coroutines.flow.StateFlow

@Composable
fun GlobalHeader(
    navigationController: NavHostController,
    pathState: StateFlow<List<String>>
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.icon {
                if (navigationController.previousBackStackEntry != null) {
                    navigationController.navigateUp()
                }
            },
            imageVector = WrIcons.backArrowDesktop,
            contentDescription = "Navigate back",
            tint = MaterialTheme.colorScheme.onBackground
        )

        PathToCurrentDirectory(pathState)
    }
}

@Composable
private fun PathToCurrentDirectory(pathState: StateFlow<List<String>>) {
    val path by pathState.collectAsState()
    val size = path.lastIndex

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 12.dp)
    ) {
        path.forEachIndexed { i, nodePath ->
            Text(
                text = nodePath,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            if (i != size) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

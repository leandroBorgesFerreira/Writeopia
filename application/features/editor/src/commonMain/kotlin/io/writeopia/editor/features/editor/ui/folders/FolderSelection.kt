package io.writeopia.editor.features.editor.ui.folders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.commonui.dtos.MenuItemUi
import io.writeopia.theme.WriteopiaTheme
import io.writeopia.ui.icons.WrSdkIcons
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FolderSelectionDialog(
    menuItemsState: StateFlow<List<MenuItemUi.FolderUi>>,
    selectedFolder: (String) -> Unit,
    expandFolder: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        FolderSelection(menuItemsState, selectedFolder, expandFolder)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderSelection(
    menuItemsState: StateFlow<List<MenuItemUi.FolderUi>>,
    selectedFolder: (String) -> Unit,
    expandFolder: (String) -> Unit,
) {
    val menuItems by menuItemsState.collectAsState()

    Column(
        Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
            .height(400.dp)
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        Text(
            "Choose folder",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(
                menuItems,
                key = { _, item -> item.id }
            ) { i, item ->
                val itemModifier = Modifier.animateItemPlacement()

                FolderItem(
                    item,
                    selectedFolder,
                    expandFolder = expandFolder,
                    modifier = itemModifier
                )
            }
        }

    }
}

@Composable
private fun FolderItem(
    folder: MenuItemUi.FolderUi,
    selectedFolder: (String) -> Unit,
    expandFolder: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val depth = folder.depth
    val bgColor = WriteopiaTheme.colorScheme.globalBackground

    Row(
        modifier = modifier
            .padding(start = 4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { selectedFolder(folder.id) }
            .background(bgColor)
            .padding(bottom = 6.dp, top = 6.dp, start = 2.dp, end = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(4.dp + 12.dp * depth))

        val imageVector = if (folder.expanded) {
            WrSdkIcons.smallArrowDown
        } else {
            WrSdkIcons.smallArrowRight
        }

        Icon(
            imageVector = imageVector,
            contentDescription = "Expand",
            tint = WriteopiaTheme.colorScheme.textLight,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    expandFolder(folder.id)
                }
                .size(26.dp)
                .padding(6.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var showIconsOptions by remember {
            mutableStateOf(false)
        }

        val tint = folder.icon?.tint?.let(::Color) ?: WriteopiaTheme.colorScheme.textLight

        Icon(
            imageVector = folder.icon?.label?.let(WrIcons::fromName) ?: WrIcons.folder,
            contentDescription = "Folder",
            tint = tint,
            modifier = Modifier.size(16.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    showIconsOptions = !showIconsOptions
                }
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = folder.title,
            modifier = Modifier,
            color = WriteopiaTheme.colorScheme.textLight,
            style = MaterialTheme.typography.bodySmall
                .copy(fontWeight = FontWeight.Bold),
            maxLines = 1
        )
    }
}

package io.writeopia.note_menu.ui.screen.menu

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.note_menu.data.model.Folder
import kotlinx.coroutines.flow.StateFlow

private const val finalWidth = 300

@Composable
fun SideGlobalMenu(
    modifier: Modifier = Modifier,
    background: Color,
    showOptions: Boolean,
    width: Dp = finalWidth.dp,
    folderClick: () -> Unit,
    favoritesClick: () -> Unit,
    settingsClick: () -> Unit,
    addFolder: () -> Unit,
    foldersState: StateFlow<List<Folder>>,
    navigateToFolder: (String) -> Unit
) {
    val widthState by derivedStateOf {
        if (showOptions) width else 0.dp
    }

    val widthAnimatedState by animateDpAsState(widthState)
    val showContent by derivedStateOf {
        widthState > width * 0.3F
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(widthAnimatedState).fillMaxHeight()) {
            if (showContent) {
                val folders by foldersState.collectAsState()

                LazyColumn(Modifier.fillMaxHeight().background(background)) {
                    item {
                        Spacer(Modifier.height(100.dp))
                    }

                    item {
                        settingsOptions(
                            iconVector = Icons.Outlined.Favorite,
                            contentDescription = "Favorites",
                            text = "Favorites",
                            click = favoritesClick
                        )
                    }

                    item {
                        settingsOptions(
                            iconVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            text = "Settings",
                            click = settingsClick,
                        )
                    }

                    item {
                        settingsOptions(
                            iconVector = null,
                            contentDescription = "Folder",
                            text = "Folder",
                            click = folderClick,
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.AddCircleOutline,
                                    contentDescription = "Add Folder",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(30.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable(onClick = addFolder)
                                        .padding(6.dp)
                                )
                            }
                        )
                    }

                    items(folders) { folder ->
                        Row(
                            modifier = Modifier.clickable {
                                navigateToFolder(folder.id)
                            }.padding(vertical = 8.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Folder,
                                contentDescription = "Folder",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = folder.title,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                                    .copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun settingsOptions(
    iconVector: ImageVector?,
    contentDescription: String,
    text: String,
    click: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(onClick = click)
            .padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth()
    ) {

        iconVector?.let { icon ->
            Icon(
                modifier = Modifier,
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1
        )

        if (trailingContent != null) {
            Spacer(modifier = Modifier.weight(1F))

            trailingContent()
        }
    }
}

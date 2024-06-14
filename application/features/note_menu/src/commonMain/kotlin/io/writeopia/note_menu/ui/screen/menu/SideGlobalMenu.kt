package io.writeopia.note_menu.ui.screen.menu

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val finalWidth = 300

@Composable
fun SideGlobalMenu(
    modifier: Modifier = Modifier,
    background: Color,
    showOptions: Boolean,
    width: Dp = finalWidth.dp,
    settingsClick: () -> Unit,
    favoritesClick: () -> Unit,
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
                Column(Modifier.fillMaxHeight().background(background)) {
                    Spacer(Modifier.height(100.dp))

                    settingsOptions(
                        iconVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        text = "Settings",
                        click = settingsClick
                    )

                    settingsOptions(
                        iconVector = Icons.Outlined.Favorite,
                        contentDescription = "Favorites",
                        text = "Favorites",
                        click = favoritesClick
                    )
                }
            }
        }
    }
}

@Composable
private fun settingsOptions(
    iconVector: ImageVector,
    contentDescription: String,
    text: String,
    click: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = click)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {

        Icon(
            modifier = Modifier,
            imageVector = iconVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1
        )
    }
}

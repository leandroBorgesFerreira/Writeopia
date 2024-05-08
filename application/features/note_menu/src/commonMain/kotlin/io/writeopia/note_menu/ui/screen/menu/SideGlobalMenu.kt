package io.writeopia.note_menu.ui.screen.menu

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SideGlobalMenu(modifier: Modifier = Modifier, background: Color) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        var showOptions by remember { mutableStateOf(true) }

        AnimatedVisibility(showOptions, modifier = Modifier.fillMaxHeight().weight(1F)) {
            Column(Modifier.fillMaxHeight().background(background)) {
                Spacer(Modifier.height(100.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { }.padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        "Settings",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .height(60.dp)
                .width(40.dp)
                .clickable {
                    showOptions = !showOptions
                },
        ) {
            VerticalDivider(
                modifier = Modifier.height(60.dp).align(Alignment.Center),
                thickness = 3.dp
            )
        }
    }
}

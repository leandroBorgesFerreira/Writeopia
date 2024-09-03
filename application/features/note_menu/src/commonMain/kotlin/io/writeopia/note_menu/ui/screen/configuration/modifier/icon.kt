package io.writeopia.note_menu.ui.screen.configuration.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun Modifier.icon(onClick: () -> Unit): Modifier =
    this.clip(CircleShape)
        .clickable(onClick = onClick)
        .size(36.dp)
        .padding(6.dp)

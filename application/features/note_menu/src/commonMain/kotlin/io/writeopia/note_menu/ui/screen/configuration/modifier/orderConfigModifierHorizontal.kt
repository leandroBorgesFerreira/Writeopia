package io.writeopia.note_menu.ui.screen.configuration.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun Modifier.orderConfigModifierHorizontal(clickable: () -> Unit): Modifier =
    composed {
        clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.inverseSurface)
            .clickable(onClick = clickable)
            .padding(6.dp)
            .fillMaxHeight()
    }

fun Modifier.orderConfigModifierVertical(onClick: () -> Unit): Modifier =
    composed {
        clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
            .fillMaxWidth()
    }

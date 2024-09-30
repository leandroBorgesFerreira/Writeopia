package io.writeopia.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun Modifier.orderConfigModifierHorizontal(clickable: () -> Unit): Modifier =
    composed {
        clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.inverseSurface)
            .clickable(onClick = clickable)
            .padding(6.dp)
            .fillMaxHeight()
    }

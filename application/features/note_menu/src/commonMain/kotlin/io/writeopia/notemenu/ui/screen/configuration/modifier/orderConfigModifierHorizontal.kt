package io.writeopia.notemenu.ui.screen.configuration.modifier

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
import io.writeopia.theme.WriteopiaTheme

fun Modifier.orderConfigModifierHorizontal(clickable: () -> Unit): Modifier =
    composed {
        clip(MaterialTheme.shapes.large)
            .background(
                WriteopiaTheme.colorScheme.defaultButton
            )
            .clickable(onClick = clickable)
            .padding(6.dp)
            .fillMaxHeight()
    }

fun Modifier.orderConfigModifierVertical(onClick: () -> Unit): Modifier =
    composed {
        clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .fillMaxWidth()
    }

package io.writeopia.sdk.utils.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
internal fun defaultTextStyle() =
    TextStyle(
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp
    )
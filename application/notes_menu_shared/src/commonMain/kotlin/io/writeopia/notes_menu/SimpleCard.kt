package io.writeopia.notes_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleCard() {
    Text(
        modifier = Modifier.background(Color.DarkGray).padding(20.dp),
        text = "This is a simple card"
    )
}
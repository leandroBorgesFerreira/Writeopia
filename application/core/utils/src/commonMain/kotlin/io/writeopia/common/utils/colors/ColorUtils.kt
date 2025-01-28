package io.writeopia.common.utils.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object ColorUtils {

    private fun availableColor() = listOf(
        Color.White,
        Color.Black,
        Color.Blue,
        Color.Gray,
        Color.Yellow,
        Color.Red,
        Color.Green,
        Color.Magenta,
        Color.DarkGray,
        Color.Cyan,
    )

    fun headerColors() = availableColor().map { it.toArgb() }

    fun tintColors() = listOf(
        Color.LightGray,
        Color.DarkGray,
        Color.Blue,
        Color.Gray,
        Color.Yellow,
        Color.Red,
        Color.Magenta,
    )
}

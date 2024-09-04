package io.writeopia.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = LIGHT_RED,
    secondary = LIGHTER_RED,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    surface = LIGHT_RED,
    inverseSurface = DARKER_RED
)

private val LightColorPalette = lightColorScheme(
    primary = LIGHT_RED,
    secondary = LIGHTER_RED,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    surface = LIGHT_RED,
    inverseSurface = DARKER_RED
)

@Immutable
data class WriteopiaColors(
    val globalBackground: Color,
)

val LocalWriteopiaColors = staticCompositionLocalOf {
    WriteopiaColors(
        globalBackground = Color.Unspecified,
    )
}

@Composable
fun WrieopiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val globalBackground = if (darkTheme) Color(0xFF252525) else Color(0xFFEEEEEE)

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }.copy(surfaceVariant = globalBackground)

    val writeopiaColors = if (darkTheme) {
        WriteopiaColors(
            globalBackground = globalBackground
        )
    } else {
        WriteopiaColors(
            globalBackground = globalBackground
        )
    }

    CompositionLocalProvider(LocalWriteopiaColors provides writeopiaColors) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

object WriteopiaTheme {
    val colorScheme: WriteopiaColors
        @Composable
        get() = LocalWriteopiaColors.current
}

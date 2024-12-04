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
    onBackground = Color(0xFF363636),
    surface = LIGHT_RED,
    inverseSurface = DARKER_RED
)

@Immutable
data class WriteopiaColors(
    val globalBackground: Color,
    val optionsSelector: Color,
    val textLight: Color,
    val highlight: Color,
    val selectedBg: Color
)

val LocalWriteopiaColors = staticCompositionLocalOf {
    WriteopiaColors(
        globalBackground = Color.Unspecified,
        optionsSelector = Color.Unspecified,
        textLight = Color.Unspecified,
        highlight = Color.Unspecified,
        selectedBg = Color.Unspecified,
    )
}

@Composable
fun WrieopiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val globalBackground = if (darkTheme) Color(0xFF252525) else Color(0xFFEEEEEE)
    val optionsSelector = if (darkTheme) Color(0x22FFFFFF) else Color(0x22000000)
    val textLight = if (darkTheme) Color(0xFFDFDFDF) else Color(0xFF444444)
    val highlight = if (darkTheme) Color(0xFF616161) else Color(0xFFE0E0E0)

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }.copy(surfaceVariant = globalBackground)

    val writeopiaColors = WriteopiaColors(
        globalBackground = globalBackground,
        optionsSelector = optionsSelector,
        textLight = textLight,
        highlight = highlight,
        selectedBg = SELECTED_BG
    )

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

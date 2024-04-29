package io.writeopia.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
  primary = LIGHT_RED,
  secondary = LIGHTER_RED,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onBackground = Color.White,
  surface = LIGHT_RED,
)

private val LightColorPalette = lightColorScheme(
  primary = LIGHT_RED,
  secondary = LIGHTER_RED,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onBackground = Color.Black,
  surface = LIGHT_RED
)

@Composable
fun WrieopiaTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  MaterialTheme(
    colorScheme = colors,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}

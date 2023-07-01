package br.com.leandroferreira.app_sample.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
  primary = ORANGE,
  secondary = DARK_ORANGE,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onBackground = Color.White,
  surface = ORANGE
)

private val LightColorPalette = lightColorScheme(
  primary = ORANGE,
  secondary = DARK_ORANGE,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onBackground = Color.Black,
  surface = ORANGE
)

@Composable
fun ApplicationComposeTheme(
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

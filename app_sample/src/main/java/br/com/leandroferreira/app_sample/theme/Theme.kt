package br.com.leandroferreira.app_sample.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
  primary = ORANGE,
  primaryVariant = ORANGE,
  secondary = ORANGE,
  onPrimary = Color.White,
  onSecondary = Color.White,
)

private val LightColorPalette = lightColors(
  primary = ORANGE,
  primaryVariant = ORANGE,
  secondary = ORANGE,
  onPrimary = Color.White,
  onSecondary = Color.White
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
    colors = colors,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}

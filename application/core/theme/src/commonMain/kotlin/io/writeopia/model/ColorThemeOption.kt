package io.writeopia.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class ColorThemeOption(val theme: String) {
    LIGHT("light"), DARK("dark"), SYSTEM("system");

    companion object {
        fun fromText(theme: String?): ColorThemeOption? = entries.find { option ->
            option.theme == theme
        }
    }
}

@Composable
fun ColorThemeOption.darkTheme(): Boolean =
    when (this) {
        ColorThemeOption.LIGHT -> true
        ColorThemeOption.DARK -> false
        ColorThemeOption.SYSTEM -> isSystemInDarkTheme()
    }

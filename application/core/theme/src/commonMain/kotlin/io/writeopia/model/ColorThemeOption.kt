package io.writeopia.model

enum class ColorThemeOption(val theme: String) {
    LIGHT("light"), DARK("dark"), SYSTEM("system");

    companion object {
        fun fromText(theme: String?): ColorThemeOption? = entries.find { option ->
            option.theme == theme
        }
    }
}

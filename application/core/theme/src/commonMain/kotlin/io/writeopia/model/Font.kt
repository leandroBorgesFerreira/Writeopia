package io.writeopia.model

enum class Font(val label: String) {
    SYSTEM("System"),
    SERIF("Serif"),
    MONOSPACE("Monospace"),
    CURSIVE("Cursive");

    companion object {
        fun fromLabel(label: String) = Font.entries.first { font -> font.label == label }
    }
}

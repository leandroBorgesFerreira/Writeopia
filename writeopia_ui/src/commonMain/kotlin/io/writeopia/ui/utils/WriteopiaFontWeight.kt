package io.writeopia.ui.utils

import androidx.compose.ui.text.font.FontWeight
import io.writeopia.sdk.models.story.WriteopiaFontWeight

fun WriteopiaFontWeight.toCompose(): FontWeight =
    when (this) {
        WriteopiaFontWeight.NORMAL -> FontWeight.Normal
        WriteopiaFontWeight.LIGHT -> FontWeight.Light
        WriteopiaFontWeight.BOLD -> FontWeight.Bold
    }
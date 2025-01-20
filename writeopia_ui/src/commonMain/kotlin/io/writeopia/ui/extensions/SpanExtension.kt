package io.writeopia.ui.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import io.writeopia.sdk.models.span.Span

fun Span.toSpanStyle(): SpanStyle =
    when (this) {
        Span.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
        Span.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
        Span.UNDERLINE -> SpanStyle(textDecoration = TextDecoration.Underline)
        Span.HIGHLIGHT -> SpanStyle(color = Color.Yellow)
    }

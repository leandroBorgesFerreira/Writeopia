package io.writeopia.ui.extensions

import androidx.compose.ui.text.TextRange
import io.writeopia.sdk.model.story.Selection
import kotlin.math.min

fun Selection.toTextRange(text: String) =
    TextRange(start = min(start, text.length), end = min(end, text.length))

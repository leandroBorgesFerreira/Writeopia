package io.writeopia.ui.extensions

import androidx.compose.ui.text.TextRange
import io.writeopia.sdk.model.story.Selection

fun Selection.toTextRange() = TextRange(start = start, end = end)

package io.writeopia.ui.model

import androidx.compose.ui.text.TextRange

data class Selection(val start: Int, val end: Int) {
    companion object {
        fun start(): Selection = Selection(0, 0)
        fun end(): Selection = Selection(Int.MAX_VALUE, Int.MAX_VALUE)
        fun fromPosition(position: Int) = Selection(position, position)
    }
}

fun Selection.toTextRange() = TextRange(start = start, end = end)

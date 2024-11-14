package io.writeopia.sdk.model.story

data class Selection(val start: SelectionStart, val end: Int) {
    companion object {
        fun start(): Selection = Selection(SelectionStart.FirstLine(0), 0)

        fun end(): Selection = Selection(SelectionStart.LastLine(Int.MAX_VALUE), Int.MAX_VALUE)

        fun fromPosition(position: Int) = Selection(position, position)
    }
}

sealed class SelectionStart(val position: Int) {
    class FirstLine(position: Int): SelectionStart(position)
    class LastLine(position: Int): SelectionStart(position)
}

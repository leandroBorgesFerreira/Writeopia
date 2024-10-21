package io.writeopia.sdk.model.story


data class Selection(val start: Int, val end: Int) {
    companion object {
        fun start(): Selection = Selection(0, 0)
        fun end(): Selection = Selection(Int.MAX_VALUE, Int.MAX_VALUE)
        fun fromPosition(position: Int) = Selection(position, position)
    }
}


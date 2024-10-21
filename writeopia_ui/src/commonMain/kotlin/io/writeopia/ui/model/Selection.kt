package io.writeopia.ui.model

data class Selection(val start: Int, val end: Int) {
    companion object {
        fun start(): Selection = Selection(0, 0)
    }
}

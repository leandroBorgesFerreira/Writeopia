package com.github.leandroborgesferreira.storyteller.drawer

data class DrawInfo(
    val editable: Boolean = true,
    val focusId: String? = null,
    val position: Int = 0,
    val extraData: Map<String, Any> = emptyMap(),
    val selectMode: Boolean = false
)

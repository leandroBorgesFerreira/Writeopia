package com.github.leandroborgesferreira.storyteller.drawer

data class DrawInfo(
    val editable: Boolean,
    val focusId: String? = null,
    val position: Int,
    val extraData: Map<String, Any> = emptyMap(),
    val selectMode: Boolean = false
)

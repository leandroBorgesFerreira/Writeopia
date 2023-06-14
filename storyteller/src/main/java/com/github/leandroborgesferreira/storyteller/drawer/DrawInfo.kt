package com.github.leandroborgesferreira.storyteller.drawer

data class DrawInfo(
    val editable: Boolean,
    val focusId: String?,
    val position: Int,
    val extraData: Map<String, Any>,
    val selectMode: Boolean
)

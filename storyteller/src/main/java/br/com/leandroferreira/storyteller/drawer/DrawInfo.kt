package br.com.leandroferreira.storyteller.drawer

data class DrawInfo(
    val editable: Boolean,
    val focusId: String?,
    val position: Int,
    val extraData: Map<String, Any>,
)

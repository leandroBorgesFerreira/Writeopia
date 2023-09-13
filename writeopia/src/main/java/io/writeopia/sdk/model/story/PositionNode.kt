package io.writeopia.sdk.model.story

data class PositionNode(
    val parentId: String,
    val position: Int,
    val nextPosition: PositionNode? = null
)
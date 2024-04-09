package io.writeopia.sdk.serialization.data

import io.writeopia.sdk.models.story.WriteopiaFontWeight
import kotlinx.serialization.Serializable

@Serializable
data class DecorationApi(
    val backgroundColor: Int? = null,
    val textSize: Int,
    val fontWeight: WriteopiaFontWeight = WriteopiaFontWeight.NORMAL
)
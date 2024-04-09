package io.writeopia.sdk.models.story

/**
 * The decoration of the [StoryStep]
 *
 * @param backgroundColor The background color.
 */
data class Decoration(
    val backgroundColor: Int,
    val textSize: Int,
    val fontWeight: WriteopiaFontWeight = WriteopiaFontWeight.NORMAL
)
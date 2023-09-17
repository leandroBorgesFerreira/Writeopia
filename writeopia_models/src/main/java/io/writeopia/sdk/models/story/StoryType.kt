package io.writeopia.sdk.models.story

/**
 * The type of a [StoryStep] you can add new types by adding a unused name and number. It is
 * recommended to use your package name before the name of the type and use any number above 100.
 *
 * @param name Name of the type
 * @param number The number of the type. Used the identify the type in a fast way.
 */
data class StoryType(val name: String, val number: Int)
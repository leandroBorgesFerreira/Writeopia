package io.storiesteller.sdk.model.story

/**
 * The state of document of the TextEditor of StoriesTeller. This class has all the stories in their
 * updated state and which one has the current focus.
 */
data class DrawState(
    val stories: Map<Int, DrawStory>,
    val focusId: String? = null
)

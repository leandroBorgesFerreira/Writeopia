package com.storiesteller.sdk.model.story

/**
 * The state of document of the TextEditor of StoryTeller. This class has all the stories in their
 * updated state and which one has the current focus.
 */
data class DrawState(
    val stories: Map<Int, DrawStory>,
    val focusId: String? = null
)

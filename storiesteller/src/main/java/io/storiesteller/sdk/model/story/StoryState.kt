package io.storiesteller.sdk.model.story

import io.storiesteller.sdk.models.story.StoryStep

/**
 * The state of document of the TextEditor of StoriesTeller. This class has all the stories in their
 * updated state and which one has the current focus.
 */
data class StoryState(
    val stories: Map<Int, StoryStep>,
    val lastEdit: LastEdit,
    val focusId: String? = null
)

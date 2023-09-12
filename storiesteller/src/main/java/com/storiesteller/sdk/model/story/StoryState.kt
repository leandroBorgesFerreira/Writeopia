package com.storiesteller.sdk.model.story

import com.storiesteller.sdk.models.story.StoryStep

/**
 * The state of document of the TextEditor of StoriesTeller. This class has all the stories in their
 * updated state and which one has the current focus.
 */
data class StoryState(
    val stories: Map<Int, StoryStep>,
    val lastEdit: LastEdit,
    val focusId: String? = null
)

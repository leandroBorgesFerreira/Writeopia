package com.github.leandroborgesferreira.storyteller.model.story

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

/**
 * The state of document of the TextEditor of StoryTeller. This class has all the stories in their
 * updated state and which one has the current focus.
 */
data class StoryState(
    val stories: Map<Int, StoryStep>,
    val lastEdit: LastEdit,
    val focusId: String? = null
)

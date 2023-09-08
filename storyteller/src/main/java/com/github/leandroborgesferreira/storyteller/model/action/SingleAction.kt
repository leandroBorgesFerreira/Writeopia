package com.github.leandroborgesferreira.storyteller.model.action

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

/**
 * An action that happens in only one [StoryStep], instead of a Collection of [StoryStep]
 */
interface SingleAction {
    val storyStep: StoryStep
    val position: Int
}

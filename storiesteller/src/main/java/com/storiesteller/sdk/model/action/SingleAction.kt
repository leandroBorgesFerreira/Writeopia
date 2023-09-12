package com.storiesteller.sdk.model.action

import com.storiesteller.sdk.models.story.StoryStep

/**
 * An action that happens in only one [StoryStep], instead of a Collection of [StoryStep]
 */
interface SingleAction {
    val storyStep: StoryStep
    val position: Int
}

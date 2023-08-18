package com.github.leandroborgesferreira.storyteller.model.action

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

interface SingleAction {
    val storyStep: StoryStep
    val position: Int
}

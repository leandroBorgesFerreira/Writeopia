package com.github.leandroferreira.storyteller.utils

import com.github.leandroferreira.storyteller.model.story.StoryType
import com.github.leandroferreira.storyteller.model.story.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space() =
        StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryType.SPACE.type,
        )
    
}

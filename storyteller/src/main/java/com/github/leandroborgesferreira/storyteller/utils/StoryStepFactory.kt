package com.github.leandroborgesferreira.storyteller.utils

import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space() =
        StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.SPACE.type,
        )
    
}

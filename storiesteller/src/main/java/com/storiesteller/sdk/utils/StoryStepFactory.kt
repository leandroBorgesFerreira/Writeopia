package com.storiesteller.sdk.utils

import com.storiesteller.sdk.model.story.StoryTypes
import com.storiesteller.sdk.models.story.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space() =
        StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.SPACE.type,
        )
    
}

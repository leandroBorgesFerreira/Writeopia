package io.storiesteller.sdk.utils

import io.storiesteller.sdk.model.story.StoryTypes
import io.storiesteller.sdk.models.story.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space() =
        StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.SPACE.type,
        )
    
}

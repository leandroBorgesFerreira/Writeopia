package io.writeopia.sdk.utils

import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep

object StoryStepFactory {

    fun space() =
        StoryStep(
            localId = GenerateId.generate(),
            type = StoryTypes.SPACE.type,
        )
    
}

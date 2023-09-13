package io.writeopia.sdk.utils

import io.writeopia.sdk.model.story.StoryTypes
import io.writeopia.sdk.models.story.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space() =
        StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.SPACE.type,
        )
    
}

package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.story.StoryType
import br.com.leandroferreira.storyteller.model.story.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space() =
        StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryType.SPACE.type,
        )
    
}

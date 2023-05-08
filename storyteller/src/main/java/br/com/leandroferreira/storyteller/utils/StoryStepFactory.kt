package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.story.StepType
import br.com.leandroferreira.storyteller.model.story.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space() =
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = StepType.SPACE.type,
        )
    
}

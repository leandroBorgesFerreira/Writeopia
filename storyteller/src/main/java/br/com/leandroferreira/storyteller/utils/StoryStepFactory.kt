package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.StepType
import br.com.leandroferreira.storyteller.model.StoryStep
import java.util.UUID

object StoryStepFactory {

    fun space(localPosition: Int) =
        StoryStep(
            id = UUID.randomUUID().toString(),
            type = StepType.SPACE.type,
            localPosition = localPosition,
        )
    
}

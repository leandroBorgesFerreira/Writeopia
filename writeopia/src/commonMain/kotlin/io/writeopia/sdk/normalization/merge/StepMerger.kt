package io.writeopia.sdk.normalization.merge

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryType

interface StepMerger {

    fun merge(step1: StoryStep, step2: StoryStep, type: StoryType): StoryStep

}

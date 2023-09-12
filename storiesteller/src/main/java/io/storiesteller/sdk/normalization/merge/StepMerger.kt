package io.storiesteller.sdk.normalization.merge

import io.storiesteller.sdk.models.story.StoryStep
import io.storiesteller.sdk.models.story.StoryType

interface StepMerger {

    fun merge(step1: StoryStep, step2: StoryStep, type: StoryType): StoryStep

}

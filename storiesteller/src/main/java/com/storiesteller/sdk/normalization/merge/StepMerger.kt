package com.storiesteller.sdk.normalization.merge

import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.models.story.StoryType

interface StepMerger {

    fun merge(step1: StoryStep, step2: StoryStep, type: StoryType): StoryStep

}

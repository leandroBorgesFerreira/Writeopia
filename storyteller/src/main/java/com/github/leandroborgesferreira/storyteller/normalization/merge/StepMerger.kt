package com.github.leandroborgesferreira.storyteller.normalization.merge

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.models.story.StoryType

interface StepMerger {

    fun merge(step1: StoryStep, step2: StoryStep, type: StoryType): StoryStep

}

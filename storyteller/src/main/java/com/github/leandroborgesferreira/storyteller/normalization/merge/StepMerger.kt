package com.github.leandroborgesferreira.storyteller.normalization.merge

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

interface StepMerger {

    fun merge(step1: StoryStep, step2: StoryStep, type: String): StoryStep

}

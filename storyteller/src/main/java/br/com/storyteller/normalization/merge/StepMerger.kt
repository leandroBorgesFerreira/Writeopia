package br.com.storyteller.normalization.merge

import br.com.storyteller.model.StoryUnit

interface StepMerger {

    fun canMerge(step1: StoryUnit, step2: StoryUnit): Boolean

    fun merge(step1: StoryUnit, step2: StoryUnit): StoryUnit
}

package br.com.storyteller.normalization.merge

import br.com.storyteller.model.GroupStep
import br.com.storyteller.model.StoryUnit

interface StepMerger {

    fun merge(step1: StoryUnit, step2: StoryUnit, type: String): StoryUnit

    fun groupsMerger(step1: GroupStep, step2: GroupStep): StoryUnit

}

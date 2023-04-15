package br.com.storyteller.normalization.merge.steps

import br.com.storyteller.model.StoryUnit
import br.com.storyteller.normalization.merge.StepMerger
import br.com.storyteller.normalization.merge.StepToGroupMerger

class ImageStepMerger(
    private val stepToGroupMerger: StepToGroupMerger = StepToGroupMerger()
) : StepMerger {

    override fun canMerge(step1: StoryUnit, step2: StoryUnit): Boolean {
        return step1.type == "image" && step2.type == "image" ||
            step1.type == "group_image" && step2.type == "image" ||
            step1.type == "image" && step2.type == "group_image"
    }

    override fun merge(step1: StoryUnit, step2: StoryUnit): StoryUnit =
        stepToGroupMerger.merge(step1, step1, "group_image")
}

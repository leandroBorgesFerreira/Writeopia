package br.com.storyteller.normalization.merge.steps

import br.com.storyteller.model.StoryUnit
import br.com.storyteller.normalization.merge.StepMerger
import br.com.storyteller.normalization.merge.StepToGroupMerger

class MessageStepMerger(
    private val stepToGroupMerger: StepToGroupMerger = StepToGroupMerger()
) : StepMerger {

    override fun canMerge(step1: StoryUnit, step2: StoryUnit): Boolean {
        return step1.type == "message" && step2.type == "message" ||
            step1.type == "group_message" && step2.type == "message" ||
            step1.type == "message" && step2.type == "group_message"
    }

    override fun merge(step1: StoryUnit, step2: StoryUnit): StoryUnit =
        stepToGroupMerger.merge(step1, step1, "group_message")
}

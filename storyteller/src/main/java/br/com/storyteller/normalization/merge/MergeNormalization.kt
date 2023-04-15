package br.com.storyteller.normalization.merge

import br.com.storyteller.model.StoryUnit

class MergeNormalization(private val stepMergers: Map<String, StepsMerger>) {

    fun mergeSteps(storySteps: List<StoryUnit>): List<StoryUnit> =
        storySteps.fold(mutableListOf()) { acc, storyStep ->
            val lastStep = acc.lastOrNull()
            val stepMerger = stepMergers[lastStep?.type]

            if (lastStep?.type != null &&
                stepMerger != null &&
                stepMerger.canMerge(lastStep, storyStep)
            ) {
                acc.apply {
                    acc.removeLast()
                    acc.add(stepMerger.merge(lastStep, storyStep))
                }
            } else {
                acc.apply { add(storyStep) }
            }
        }

    class Builder() {
        private val stepMergers: MutableMap<String, StepsMerger> = mutableMapOf()

        fun addMerger(type: String, stepMerger: StepsMerger) {
            stepMergers[type] = stepMerger
        }

        fun build(): MergeNormalization = MergeNormalization(stepMergers)
    }
}

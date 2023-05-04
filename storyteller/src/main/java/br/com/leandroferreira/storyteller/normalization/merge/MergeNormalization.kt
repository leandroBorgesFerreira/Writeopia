package br.com.leandroferreira.storyteller.normalization.merge

import br.com.leandroferreira.storyteller.model.StoryUnit


/**
 *
 */
class MergeNormalization(private val stepMergers: Set<StepsMergerCoordinator>) {

    fun mergeSteps(storySteps: Iterable<StoryUnit>): List<StoryUnit> =
        // First, group all the elements by its position
        storySteps.groupBy { storyStep -> storyStep.localPosition }
            // Then reduce all the possible steps in the same position
            .mapValues { (_, steps) -> reducePossibleSteps(steps) }
            // At last, create the final list by merge all the intermediate lists.
            .values
            .reduce(Collection<StoryUnit>::plus)

    /**
     * Note that it may happen that some elements in the same position may not be able to be
     * merged, that's why this method returns a List<StoryUnit> instead of StoryUnit.
     */
    private fun reducePossibleSteps(steps: List<StoryUnit>): List<StoryUnit> {
        return steps.fold(mutableListOf()) { acc, storyStep ->
            val lastStep = acc.lastOrNull()

            if (lastStep?.type != null &&
                stepMergers.any { it.canMerge(lastStep, storyStep) }
            ) {
                acc.apply {
                    acc.removeLast()
                    acc.add(
                        stepMergers
                            .first { merger -> merger.canMerge(lastStep, storyStep) }
                            .merge(lastStep, storyStep)
                    )
                }
            } else {
                acc.apply { add(storyStep) }
            }
        }
    }


    companion object {
        fun build(buildFunc: Builder.() -> Unit) = Builder().apply(buildFunc).build()
    }

    class Builder internal constructor() {
        private val stepMergers: MutableSet<StepsMergerCoordinator> = mutableSetOf()

        fun addMerger(stepMerger: StepsMergerCoordinator): Builder = apply {
            stepMergers.add(stepMerger)
        }

        fun build(): MergeNormalization = MergeNormalization(stepMergers)
    }
}

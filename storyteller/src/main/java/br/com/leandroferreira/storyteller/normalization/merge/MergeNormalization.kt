package br.com.leandroferreira.storyteller.normalization.merge

import br.com.leandroferreira.storyteller.model.StoryUnit


/**
 * 
 */
class MergeNormalization(private val stepMergers: Set<StepsMergerCoordinator>) {

    //Todo: This a bad logic because it only works is the elements are ordered, which shouldn't be
    // a constraint.
    fun mergeSteps(storySteps: Iterable<StoryUnit>): List<StoryUnit> {
        val stepsByGroup = storySteps.groupBy { it.localPosition }
        val reducedSteps = stepsByGroup.mapValues { (_, steps) -> reducePossibleSteps(steps) }
        return reducedSteps.values.reduce { list1, list2 -> list1 + list2 }
    }

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

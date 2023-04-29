package br.com.leandroferreira.storyteller.normalization.split

import br.com.leandroferreira.storyteller.model.GroupStep
import br.com.leandroferreira.storyteller.model.StoryUnit

object GroupSplitterNormalization {

    fun split(storySteps: Iterable<StoryUnit>): List<StoryUnit> =
        storySteps.fold<StoryUnit, List<StoryUnit>>(listOf()) { acc, storyUnit ->
            if (storyUnit is GroupStep) {
                groupToList(storyUnit)
            } else {
                acc + storyUnit
            }
        }.mapIndexed { index, storyUnit ->
            storyUnit.copyWithNewPosition(index)
        }


    /**
     * This method separated the GroupStep when they don't all have the same position. New GroupSteps
     * and StorySteps will be created accordingly with the position that was passed along. Please
     * be aware that this method will create all new StoryUnits with the same position, to it is
     * needed to normalize the positions in a latter moment.
     */
    private fun groupToList(groupStep: GroupStep): List<StoryUnit> {
        return groupStep.steps.groupBy { storyUnit -> storyUnit.localPosition }
            .toSortedMap { position1, position2 -> position1.compareTo(position2) }
            .map { (position, storyList) ->
                if (storyList.size == 1) {
                    storyList.first().copyWithNewPosition(groupStep.localPosition)
                } else {
                    GroupStep(
                        id = "apksd",  //Todo: Generate an id latter.
                        type = groupStep.type,
                        localPosition = groupStep.localPosition,
                        steps = storyList
                    )
                }
            }
    }
}

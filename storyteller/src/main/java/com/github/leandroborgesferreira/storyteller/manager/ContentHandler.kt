package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.change.DeleteInfo
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.utils.UnitsNormalizationMap
import com.github.leandroborgesferreira.storyteller.utils.extensions.toEditState

/**
 * Class dedicated to handle adding and/or deleting new StorySteps
 */
class ContentHandler(
    private val focusableTypes: Set<String>,
    private val stepsNormalizer: UnitsNormalizationMap
) {

    fun deleteStory(deleteInfo: DeleteInfo, history: Map<Int, StoryStep>): StoryState? {
        val step = deleteInfo.storyUnit
        val parentId = step.parentId
        val mutableSteps = history.toMutableMap()

        return if (parentId == null) {
            mutableSteps.remove(deleteInfo.position)
            val previousFocus =
                FindStory.previousFocus(
                    history.values.toList(),
                    deleteInfo.position,
                    focusableTypes
                )

            val normalized = stepsNormalizer(mutableSteps.toEditState())
            StoryState(normalized, focusId = previousFocus?.id)
        } else {
            mutableSteps[deleteInfo.position]?.let { group ->
                val newSteps = group.steps.filter { storyUnit ->
                    storyUnit.localId != step.localId
                }

                val newStoryUnit = if (newSteps.size == 1) {
                    newSteps.first()
                } else {
                    group.copy(steps = newSteps)
                }

                mutableSteps[deleteInfo.position] = newStoryUnit.copy(parentId = null)
                StoryState(stepsNormalizer(mutableSteps.toEditState()))
            }
        }

    }
}
package com.github.leandroborgesferreira.storyteller.manager

import android.widget.Space
import com.github.leandroborgesferreira.storyteller.model.change.DeleteInfo
import com.github.leandroborgesferreira.storyteller.model.change.LineBreakInfo
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.utils.StoryStepFactory
import com.github.leandroborgesferreira.storyteller.utils.alias.UnitsNormalizationMap
import com.github.leandroborgesferreira.storyteller.utils.extensions.associateWithPosition
import com.github.leandroborgesferreira.storyteller.utils.extensions.toEditState
import com.github.leandroborgesferreira.storyteller.utils.iterables.MapOperations
import java.util.UUID

/**
 * Class dedicated to handle adding and/or deleting new StorySteps
 */
class ContentHandler(
    private val focusableTypes: Set<String>,
    private val stepsNormalizer: UnitsNormalizationMap
) {

    fun createCheckItem(currentStory: Map<Int, StoryStep>, position: Int): StoryState {
        val newMap = currentStory.toMutableMap()
        val newCheck = StoryStep(
            id = UUID.randomUUID().toString(),
            localId = UUID.randomUUID().toString(),
            type = StoryType.CHECK_ITEM.type,
        )
        newMap[position] = newCheck

        return StoryState(newMap, newCheck.id)
    }

    //Todo: Add unit test
    fun addNewContent(
        currentStory: Map<Int, StoryStep>,
        newStoryUnit: StoryStep,
        position: Int
    ): Map<Int, StoryStep> =
        MapOperations.addElementInPosition(
            currentStory,
            newStoryUnit,
            StoryStepFactory.space(),
            position
        )

    fun addNewContentBulk(
        currentStory: Map<Int, StoryStep>,
        newStory: Map<Int, StoryStep>,
        addInBetween: () -> StoryStep
    ): Map<Int, StoryStep> = MapOperations.mergeSortedMaps(currentStory, newStory, addInBetween)

    fun onLineBreak(
        currentStory: Map<Int, StoryStep>,
        lineBreakInfo: LineBreakInfo
    ): Pair<Pair<Int, StoryStep>, StoryState>? {
        val storyStep = lineBreakInfo.storyStep

        return storyStep.text?.split("\n", limit = 2)?.let { list ->
            val secondText = list.elementAtOrNull(1) ?: ""
            val secondMessage = StoryStep(
                localId = UUID.randomUUID().toString(),
                type = storyStep.type,
                text = secondText,
            )

            val position = lineBreakInfo.position + 1

            val newStory = addNewContent(
                currentStory,
                secondMessage,
                position
            )

            (position to secondMessage) to StoryState(
                stories = newStory,
                focusId = secondMessage.id
            )
        }
    }

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

    fun bulkDeletion(
        positions: Iterable<Int>,
        stories: Map<Int, StoryStep>
    ): Pair<Map<Int, StoryStep>, Map<Int, StoryStep>> {
        val deleted = mutableMapOf<Int, StoryStep>()
        val mutable = stories.toMutableMap()
        positions.forEach { position ->
            mutable.remove(position)?.let { deletedStory ->
                deleted[position] = deletedStory
            }
        }

        return mutable to deleted
    }
}
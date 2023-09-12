package com.storiesteller.sdk.manager

import com.storiesteller.sdk.model.action.Action
import com.storiesteller.sdk.model.command.CommandInfo
import com.storiesteller.sdk.model.command.CommandTrigger
import com.storiesteller.sdk.model.story.LastEdit
import com.storiesteller.sdk.model.story.StoryState
import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.models.story.StoryType
import com.storiesteller.sdk.model.story.StoryTypes
import com.storiesteller.sdk.utils.StoryStepFactory
import com.storiesteller.sdk.utils.alias.UnitsNormalizationMap
import com.storiesteller.sdk.utils.extensions.toEditState
import com.storiesteller.sdk.utils.iterables.MapOperations
import java.util.UUID

/**
 * Class dedicated to handle adding, deleting or changing StorySteps
 */
class ContentHandler(
    private val focusableTypes: Set<Int> = setOf(
        StoryTypes.TITLE.type.number,
        StoryTypes.MESSAGE.type.number,
        StoryTypes.H1.type.number,
        StoryTypes.H2.type.number,
        StoryTypes.H3.type.number,
        StoryTypes.H4.type.number,
        StoryTypes.CHECK_ITEM.type.number,
        StoryTypes.UNORDERED_LIST_ITEM.type.number,
    ),
    private val stepsNormalizer: UnitsNormalizationMap,
    private val lineBreakMap: (StoryType) -> StoryType = ::defaultLineBreakMap
) {

    fun changeStoryStepState(
        currentStory: Map<Int, StoryStep>,
        newState: StoryStep,
        position: Int
    ): StoryState? {
        return if (currentStory[position] != null) {
            val newMap = currentStory.toMutableMap()
            newMap[position] = newState
            StoryState(newMap, LastEdit.LineEdition(position, newState), null)
        } else {
            null
        }
    }

    fun changeStoryType(
        currentStory: Map<Int, StoryStep>,
        type: StoryType,
        position: Int,
        commandInfo: CommandInfo
    ): StoryState {
        val newMap = currentStory.toMutableMap()
        val storyStep = newMap[position]
        val commandTrigger = commandInfo.commandTrigger
        val commandText = commandInfo.command.commandText

        if (storyStep != null) {
            val storyText = storyStep.text
            val newText = if (
                commandTrigger == CommandTrigger.WRITTEN &&
                storyText?.startsWith(commandText) == true
            ) {
                storyText.subSequence(commandText.length, storyText.length).toString()
            } else {
                storyStep.text
            }

            val newCheck = storyStep.copy(
                localId = UUID.randomUUID().toString(),
                type = type,
                text = newText
            )

            newMap[position] = newCheck
        }

        return StoryState(newMap, LastEdit.Whole, storyStep?.id)
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
        lineBreakInfo: Action.LineBreak
    ): Pair<Pair<Int, StoryStep>, StoryState>? {
        val storyStep = lineBreakInfo.storyStep

        return storyStep.text?.split("\n", limit = 2)?.let { list ->
            val firstText = list.elementAtOrNull(0) ?: ""
            val secondText = list.elementAtOrNull(1) ?: ""
            val secondMessage = StoryStep(
                localId = UUID.randomUUID().toString(),
                type = lineBreakMap(storyStep.type),
                text = secondText,
            )

            val addPosition = lineBreakInfo.position + 2

            //Todo: Cover this in unit tests!
            if (currentStory[addPosition]?.type == StoryTypes.SPACE.type) {
                throw IllegalStateException(
                    "it should not be possible to add content in the place of a space"
                )
            }

            val mutable = currentStory.toMutableMap().apply {
                this[lineBreakInfo.position] = storyStep.copy(text = firstText)
            }

            val newStory = addNewContent(
                mutable,
                secondMessage,
                addPosition
            )

            (addPosition to secondMessage) to StoryState(
                stories = newStory,
                lastEdit = LastEdit.Whole,
                focusId = secondMessage.id
            )
        }
    }

    fun deleteStory(deleteInfo: Action.DeleteStory, history: Map<Int, StoryStep>): StoryState? {
        val step = deleteInfo.storyStep
        val parentId = step.parentId
        val mutableSteps = history.toMutableMap()

        return if (parentId == null) {
            mutableSteps.remove(deleteInfo.position)
            val previousFocus =
                FindStory.previousFocus(
                    mutableSteps.values.toList(),
                    deleteInfo.position,
                    focusableTypes
                )

            val normalized = stepsNormalizer(mutableSteps.toEditState())
            StoryState(normalized, lastEdit = LastEdit.Whole, focusId = previousFocus?.id)
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
                StoryState(stepsNormalizer(mutableSteps.toEditState()), lastEdit = LastEdit.Whole)
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

            mutable.remove(position + 1)
        }

        return mutable to deleted
    }
}

private fun defaultLineBreakMap(storyType: StoryType): StoryType =
    when(storyType) {
        StoryTypes.H1.type,
        StoryTypes.H2.type,
        StoryTypes.H3.type,
        StoryTypes.H4.type,
        StoryTypes.TITLE.type -> StoryTypes.MESSAGE.type
        else -> storyType
    }

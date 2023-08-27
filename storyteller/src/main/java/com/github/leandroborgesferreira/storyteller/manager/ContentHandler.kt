package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.command.CommandInfo
import com.github.leandroborgesferreira.storyteller.model.command.CommandTrigger
import com.github.leandroborgesferreira.storyteller.model.story.LastEdit
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.models.story.StoryType
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.utils.StoryStepFactory
import com.github.leandroborgesferreira.storyteller.utils.alias.UnitsNormalizationMap
import com.github.leandroborgesferreira.storyteller.utils.extensions.toEditState
import com.github.leandroborgesferreira.storyteller.utils.iterables.MapOperations
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

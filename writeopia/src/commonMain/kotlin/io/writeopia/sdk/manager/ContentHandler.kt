package io.writeopia.sdk.manager

import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.story.LastEdit
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.command.CommandTrigger
import io.writeopia.sdk.models.command.TypeInfo
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.utils.alias.UnitsNormalizationMap
import io.writeopia.sdk.utils.extensions.previousTextStory
import io.writeopia.sdk.utils.extensions.toEditState
import io.writeopia.sdk.utils.iterables.addElementInPosition
import io.writeopia.sdk.utils.iterables.mergeSortedMaps
import io.writeopia.sdk.utils.iterables.normalizePositions

/**
 * Class dedicated to handle adding, deleting or changing StorySteps
 */
class ContentHandler(
    private val focusableTypes: Set<Int> = setOf(
        StoryTypes.TITLE.type.number,
        StoryTypes.TEXT.type.number,
        StoryTypes.CHECK_ITEM.type.number,
        StoryTypes.UNORDERED_LIST_ITEM.type.number,
    ),
    private val stepsNormalizer: UnitsNormalizationMap,
    private val lineBreakMap: (StoryType) -> StoryType = ::defaultLineBreakMap,
    private val isTextStory: (StoryStep) -> Boolean = { story ->
        focusableTypes.contains(story.type.number)
    }
) {

    fun changeStoryStepState(
        currentStory: Map<Int, StoryStep>,
        newState: StoryStep,
        position: Int
    ): StoryState? {
        return if (currentStory[position] != null) {
            val newMap = currentStory.toMutableMap()
            newMap[position] = newState
            StoryState(newMap, LastEdit.LineEdition(position, newState), newState.id)
        } else {
            null
        }
    }

    fun changeStoryType(
        currentStory: Map<Int, StoryStep>,
        typeInfo: TypeInfo,
        position: Int,
        commandInfo: CommandInfo?
    ): StoryState {
        val newMap = currentStory.toMutableMap()
        val storyStep = newMap[position]
        val commandTrigger = commandInfo?.commandTrigger
        val commandText = commandInfo?.command?.commandText ?: ""

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

            val decoration = typeInfo.decoration

            val newCheck = if (decoration != null) {
                storyStep.copy(
                    localId = GenerateId.generate(),
                    type = typeInfo.storyType,
                    text = newText,
                    decoration = decoration
                )
            } else {
                storyStep.copy(
                    localId = GenerateId.generate(),
                    type = typeInfo.storyType,
                    text = newText,
                )
            }

            newMap[position] = newCheck
        }

        return StoryState(newMap, LastEdit.Whole, storyStep?.id)
    }

    // Todo: Add unit test
    fun addNewContent(
        currentStory: Map<Int, StoryStep>,
        newStoryUnit: StoryStep,
        position: Int
    ): Map<Int, StoryStep> = currentStory.addElementInPosition(newStoryUnit, position)

    fun addNewContentBulk(
        currentStory: Map<Int, StoryStep>,
        newStory: Map<Int, StoryStep>,
    ): Map<Int, StoryStep> = currentStory.mergeSortedMaps(newStory)

    fun onLineBreak(
        currentStory: Map<Int, StoryStep>,
        lineBreakInfo: Action.LineBreak
    ): Pair<Pair<Int, StoryStep>, StoryState>? {
        val storyStep = lineBreakInfo.storyStep

        return storyStep.text?.split("\n", limit = 2)?.let { list ->
            val firstText = list.elementAtOrNull(0) ?: ""
            val secondText = list.elementAtOrNull(1) ?: ""
            val secondMessage = StoryStep(
                localId = GenerateId.generate(),
                type = lineBreakMap(storyStep.type),
                text = secondText,
            )

            val addPosition = lineBreakInfo.position + 1

            // Todo: Cover this in unit tests!
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
            val previousFocus: StoryStep? =
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

    fun eraseStory(deleteInfo: Action.EraseStory, history: Map<Int, StoryStep>): StoryState {
        val mutableSteps = history.toMutableMap()

        mutableSteps.remove(deleteInfo.position)
        val previousFocus: StoryStep? =
            FindStory.previousFocus(
                mutableSteps.values.toList(),
                deleteInfo.position,
                focusableTypes
            )

        history.previousTextStory(deleteInfo.position, isTextStory)?.let { (previous, position) ->
            mutableSteps[position] = previous.copy(
                text = previous.text + deleteInfo.storyStep.text,
                localId = GenerateId.generate()
            )
        }

        val normalized = stepsNormalizer(mutableSteps.toEditState())
        return StoryState(normalized, lastEdit = LastEdit.Whole, focusId = previousFocus?.id)
    }

    /**
     * Delete story steps in bulk. Returns a pair with first the new state of stories and
     * the deleted stories.
     */
    fun bulkDeletion(
        positions: Iterable<Int>,
        stories: Map<Int, StoryStep>
    ): Pair<Map<Int, StoryStep>, Map<Int, StoryStep>> {
        val deleted = mutableMapOf<Int, StoryStep>()
        val newState = stories.toMutableMap()

        positions.forEach { position ->
            newState.remove(position)?.let { deletedStory ->
                deleted[position] = deletedStory
            }
        }

        return newState.normalizePositions() to deleted
    }

    fun previousTextStory(
        storyMap: Map<Int, StoryStep>,
        position: Int
    ) = storyMap.previousTextStory(position, isTextStory)
}

private fun defaultLineBreakMap(storyType: StoryType): StoryType =
    when (storyType) {
        StoryTypes.TITLE.type -> StoryTypes.TEXT.type

        else -> storyType
    }

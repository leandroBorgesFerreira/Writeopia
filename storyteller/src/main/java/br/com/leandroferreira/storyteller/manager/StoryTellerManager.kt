package br.com.leandroferreira.storyteller.manager

import br.com.leandroferreira.storyteller.model.change.CheckInfo
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryState
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryType
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.normalization.builder.StepsMapNormalizationBuilder
import br.com.leandroferreira.storyteller.utils.StoryStepFactory
import br.com.leandroferreira.storyteller.utils.UnitsNormalizationMap
import br.com.leandroferreira.storyteller.utils.extensions.associateWithPosition
import br.com.leandroferreira.storyteller.utils.extensions.toEditState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class StoryTellerManager(
    private val stepsNormalizer: UnitsNormalizationMap =
        StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val focusableTypes: Set<String> = setOf(
        StoryType.CHECK_ITEM.type,
        StoryType.MESSAGE.type
    )
) {

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _normalizedSteps: MutableStateFlow<StoryState> = MutableStateFlow(
        StoryState(stories = emptyMap())
    )
    val normalizedStepsState: StateFlow<StoryState> = _normalizedSteps.asStateFlow()

    fun addStories(stories: Map<Int, StoryUnit>) {
        _normalizedSteps.value = StoryState(stepsNormalizer(stories.toEditState()), null)
    }

    fun mergeRequest(info: MergeInfo) {
        val sender = info.sender
        val receiver = info.receiver
        val positionTo = info.positionTo
        val positionFrom = info.positionFrom

        if (info.positionFrom == info.positionTo) return

        val mutableHistory = _normalizedSteps.value.stories.toEditState()
        val receiverStepList = mutableHistory[positionTo]
        receiverStepList?.plus(sender.copyWithNewParent(receiver.parentId))?.let { newList ->
            mutableHistory[positionTo] = newList
        }

        if (sender.parentId == null) {
            mutableHistory.remove(positionFrom)
        } else {
            val fromGroup = (mutableHistory[positionFrom]?.first() as? GroupStep)
            val newList = fromGroup?.steps?.filter { storyUnit -> storyUnit.localId != sender.localId }

            if (newList != null) {
                mutableHistory[positionFrom] = listOf(fromGroup.copy(steps = newList))
            }
        }

        _normalizedSteps.value = StoryState(stories = stepsNormalizer(mutableHistory))
    }

    fun moveRequest(moveInfo: MoveInfo) {
        val mutable = _normalizedSteps.value.stories.toMutableMap()
        if (mutable[moveInfo.positionTo]?.type != "space") throw IllegalStateException()

        mutable[moveInfo.positionTo] = moveInfo.storyUnit.copyWithNewParent(null)

        if (moveInfo.storyUnit.parentId == null) {
            mutable.remove(moveInfo.positionFrom)
        } else {
            val fromGroup = (mutable[moveInfo.positionFrom] as? GroupStep)
            val newList = fromGroup?.steps?.filter { storyUnit ->
                storyUnit.localId != moveInfo.storyUnit.localId
            }

            if (newList != null) {
                mutable[moveInfo.positionFrom] = fromGroup.copy(steps = newList)
            }
        }

        _normalizedSteps.value = StoryState(stepsNormalizer(mutable.toEditState()))
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     */
    fun checkRequest(checkInfo: CheckInfo) {
        updateState()
        val storyUnit = checkInfo.storyUnit

        val newStep = (storyUnit as? StoryStep)?.copy(checked = checkInfo.checked) ?: return
        val newMap = _normalizedSteps.value.stories.toMutableMap()

        newMap[checkInfo.position] = newStep

        _normalizedSteps.value = StoryState(newMap)
    }

    fun onTextEdit(text: String, position: Int) {
        textChanges[position] = text
    }

    fun onLineBreak(lineBreakInfo: LineBreakInfo) {
        val updatedStories = updateTexts(_normalizedSteps.value.stories)
        _normalizedSteps.value = separateMessages(updatedStories.stories, lineBreakInfo)
    }

    private fun separateMessages(
        stories: Map<Int, StoryUnit>,
        lineBreakInfo: LineBreakInfo
    ): StoryState {
        val storyStep = lineBreakInfo.storyStep
        storyStep.text?.split("\n", limit = 2)?.let { list ->
            var acc = lineBreakInfo.position + 1

            val secondText = list.elementAtOrNull(1) ?: ""

            val mutable = stories.values.toMutableList()
            mutable.add(
                acc++,
                StoryStepFactory.space()
            )

            val secondMessage = StoryStep(
                localId = UUID.randomUUID().toString(),
                type = storyStep.type,
                text = secondText,
            )
            mutable.add(acc, secondMessage)

            return StoryState(
                stories = mutable.associateWithPosition(),
                focusId = secondMessage.id
            )
        }

        return StoryState(stories)
    }

    fun updateState() {
        _normalizedSteps.value = updateTexts(_normalizedSteps.value.stories)
    }

    private fun updateTexts(stepMap: Map<Int, StoryUnit>): StoryState {
        val mutableSteps = stepMap.toMutableMap()

        textChanges.forEach { (position, text) ->
            val editStep = mutableSteps[position]

            (editStep as? StoryStep)?.copy(text = text)?.let { step ->
                mutableSteps[position] = step
            }
        }

        textChanges.clear()

        return StoryState(mutableSteps)
    }

    fun onDelete(deleteInfo: DeleteInfo) {
        val newSteps = updateTexts(_normalizedSteps.value.stories)
        delete(deleteInfo, newSteps.stories)
    }

    private fun delete(
        deleteInfo: DeleteInfo,
        history: Map<Int, StoryUnit>,
    ) {
        val step = deleteInfo.storyUnit
        val parentId = step.parentId
        val mutableSteps = history.toMutableMap()

        if (parentId == null) {
            mutableSteps.remove(deleteInfo.position)
            val previousFocus =
                FindStory.previousFocus(history.values.toList(), deleteInfo.position, focusableTypes)
            val normalized = stepsNormalizer(mutableSteps.toEditState())

            _normalizedSteps.value = StoryState(normalized, focusId = previousFocus?.id)
        } else {
            (mutableSteps[deleteInfo.position] as? GroupStep)?.let { group ->
                val newSteps = group.steps.filter { storyUnit ->
                    storyUnit.localId != step.localId
                }

                val newStoryUnit = if (newSteps.size == 1) {
                    newSteps.first()
                } else {
                    group.copy(steps = newSteps)
                }

                mutableSteps[deleteInfo.position] = newStoryUnit.copyWithNewParent(null)
                _normalizedSteps.value =
                    StoryState(stepsNormalizer(mutableSteps.toEditState()))
            }
        }
    }
}

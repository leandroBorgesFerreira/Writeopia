package br.com.leandroferreira.storyteller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.model.change.CheckInfo
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStateMap
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.normalization.builder.StepsMapNormalizationBuilder
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.utils.StoryStepFactory
import br.com.leandroferreira.storyteller.utils.UnitsNormalizationMap
import br.com.leandroferreira.storyteller.utils.extensions.associateWithPosition
import br.com.leandroferreira.storyteller.utils.extensions.toEditState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class StoryTellerViewModel(
    private val storiesRepository: StoriesRepository,
    private val stepsMapNormalizer: UnitsNormalizationMap =
        StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
) : ViewModel() {

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _normalizedSteps: MutableStateFlow<StoryStateMap> = MutableStateFlow(
        StoryStateMap(stories = emptyMap())
    )
    val normalizedStepsState: StateFlow<StoryStateMap> = _normalizedSteps.asStateFlow()

    fun requestHistoriesFromApi(
        force: Boolean = false,
        normalizer: UnitsNormalizationMap = StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }
    ) {
        if (_normalizedSteps.value.stories.isEmpty() || force) {
            viewModelScope.launch {
                val normalizedSteps = normalizer(storiesRepository.history().toEditState())
                _normalizedSteps.value = StoryStateMap(normalizedSteps)
            }
        }
    }

    fun mergeRequest(info: MergeInfo) {
        val sender = info.sender
        val receiver = info.receiver

        if (info.positionFrom == info.positionTo) return

        /* Search by position doesn't work because a receiver may be inside a GroupStep
        * Note: The search can speed up by using the position */
        FindStory.findById(_normalizedSteps.value.stories.values, receiver.id)
            ?.let { (receiver, parentReceiver) ->
                when {
                    parentReceiver != null -> {
                        _normalizedSteps.value =
                            StoryStateMap(
                                stories = mergeStep(receiver, sender, info.positionTo, info.positionFrom),
                            )
                    }

                    receiver != null -> {
                        _normalizedSteps.value =
                            StoryStateMap(
                                stories = mergeStep(receiver, sender, info.positionTo, info.positionFrom),
                            )
                    }
                }
            }
    }

    private fun mergeStep(
        receiver: StoryUnit?,
        sender: StoryUnit,
        positionTo: Int,
        positionFrom: Int
    ): Map<Int, StoryUnit> {
        val mutableHistory = _normalizedSteps.value.stories.toEditState()
        val receiverStepList = mutableHistory[positionTo]
        receiverStepList?.plus(sender.copyWithNewParent(receiver?.parentId))?.let { newList ->
            mutableHistory[positionTo] = newList
        }

        if (sender.parentId == null) {
            mutableHistory.remove(positionFrom)
        } else {
            val fromGroup = (mutableHistory[positionFrom]?.first() as? GroupStep)
            val newList = fromGroup?.steps?.filter { storyUnit -> storyUnit.id != sender.id }

            if (newList != null) {
                mutableHistory[positionFrom] = listOf(fromGroup.copy(steps = newList))
            }
        }

        return stepsMapNormalizer(mutableHistory)
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
                storyUnit.id != moveInfo.storyUnit.id
            }

            if (newList != null) {
                mutable[moveInfo.positionFrom] = fromGroup.copy(steps = newList)
            }
        }

        _normalizedSteps.value = StoryStateMap(stepsMapNormalizer(mutable.toEditState()))
    }

    fun checkRequest(checkInfo: CheckInfo) {
        updateState()
        val parentId = checkInfo.storyUnit.parentId

        val storyUnit = if (parentId != null) {
            FindStory.findById(_normalizedSteps.value.stories.values, parentId)?.first ?: return
        } else {
            checkInfo.storyUnit
        }

        val newStep = (storyUnit as? StoryStep)?.copy(checked = checkInfo.checked) ?: return
        val newMap = _normalizedSteps.value.stories.toMutableMap()

        newMap[checkInfo.position] = newStep

        _normalizedSteps.value = StoryStateMap(newMap)

    }

    fun onTextEdit(text: String, position: Int) {
        textChanges[position] = text
    }

    //Todo: Add unit test for this method!
    fun onLineBreak(lineBreakInfo: LineBreakInfo) {
        val updatedStories = updateTexts(_normalizedSteps.value.stories)
        _normalizedSteps.value = separateMessages(updatedStories.stories, lineBreakInfo)
    }

    private fun separateMessages(
        stories: Map<Int, StoryUnit>,
        lineBreakInfo: LineBreakInfo
    ): StoryStateMap {
        val storyStep = lineBreakInfo.storyStep
        storyStep.text?.split("\n", limit = 2)?.let { list ->
            val secondText = list.elementAtOrNull(1) ?: ""

            val mutable = stories.values.toMutableList()
            mutable.add(
                lineBreakInfo.position + 1,
                StoryStepFactory.space()
            )

            val secondMessage = StoryStep(
                id = UUID.randomUUID().toString(),
                type = storyStep.type,
                text = secondText,
            )
            mutable.add(lineBreakInfo.position + 1, secondMessage)

            return StoryStateMap(
                stories = stepsMapNormalizer(mutable.associateWithPosition().toEditState()),
                focusId = secondMessage.id
            )
        }

        return StoryStateMap(stories)
    }

    fun updateState() {
        _normalizedSteps.value = updateTexts(_normalizedSteps.value.stories)
    }

    private fun updateTexts(stepMap: Map<Int, StoryUnit>): StoryStateMap {
        val mutableSteps = stepMap.toMutableMap()

        textChanges.forEach { (position, text) ->
            val editStep = mutableSteps[position]

            (editStep as? StoryStep)?.copy(text = text)?.let { step ->
                mutableSteps[position] = step
            }
        }

        textChanges.clear()

        return StoryStateMap(mutableSteps)
    }

    fun onDelete(deleteInfo: DeleteInfo) {
        updateState()
        delete(deleteInfo, _normalizedSteps.value.stories)
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
                FindStory.previousFocus(history.values.toList(), deleteInfo.position)
            val normalized = stepsMapNormalizer(mutableSteps.toEditState())

            _normalizedSteps.value = StoryStateMap(normalized, focusId = previousFocus?.id)
        } else {
            FindStory.findById(history.values, parentId)
                ?.first
                ?.let { group ->
                    val newSteps = (group as GroupStep).steps.filter { storyUnit ->
                        storyUnit.id != step.id
                    }

                    val newStoryUnit = if (newSteps.size == 1) {
                        newSteps.first()
                    } else {
                        group.copy(steps = newSteps)
                    }

                    mutableSteps[deleteInfo.position] = newStoryUnit.copyWithNewParent(null)
                    _normalizedSteps.value =
                        StoryStateMap(stepsMapNormalizer(mutableSteps.toEditState()))
                }
        }
    }
}

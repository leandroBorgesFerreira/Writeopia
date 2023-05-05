package br.com.leandroferreira.storyteller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStateMap
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.model.change.CheckInfo
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.normalization.builder.StepsMapNormalizationBuilder
import br.com.leandroferreira.storyteller.repository.StoriesRepository
import br.com.leandroferreira.storyteller.utils.StoryStepFactory
import br.com.leandroferreira.storyteller.utils.UnitsNormalizationMap
import br.com.leandroferreira.storyteller.utils.extensions.associateWithPosition
import br.com.leandroferreira.storyteller.utils.extensions.toEditState
import br.com.leandroferreira.storyteller.viewmodel.move.MoveHandler
import br.com.leandroferreira.storyteller.viewmodel.move.SpaceMoveHandler
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
    private val moveHandler: MoveHandler = SpaceMoveHandler(),
) : ViewModel() {

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _normalizedStepsMap: MutableStateFlow<StoryStateMap> = MutableStateFlow(
        StoryStateMap(stories = emptyMap())
    )
    val normalizedStepsStateMap: StateFlow<StoryStateMap> = _normalizedStepsMap.asStateFlow()

//    private val _scrollToPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
//    val scrollToPosition: StateFlow<Int?> = _scrollToPosition.asStateFlow()

    fun requestHistoriesFromApi(
        force: Boolean = false,
        normalizer: UnitsNormalizationMap = StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        }
    ) {
        if (_normalizedStepsMap.value.stories.isEmpty() || force) {
            viewModelScope.launch {
                val normalizedSteps = normalizer(storiesRepository.historyMap().toEditState())
                _normalizedStepsMap.value = StoryStateMap(normalizedSteps)
            }
        }
    }

    /*
     * Todo: Refactor this. Now that I'm using a Map each drawer can receive the position and return
     * to the merge request
     */
    fun mergeRequest(info: MergeInfo) {
        val sender = info.sender
        val receiver = info.receiver

        /* Search by position doesn't work because a receiver may be inside a GroupStep
        * Note: The search can speed up by using the position */
        FindStory.findById(_normalizedStepsMap.value.stories.values, receiver.id)
            ?.let { (receiver, parentReceiver) ->
                when {
                    parentReceiver != null -> {
                        _normalizedStepsMap.value =
                            StoryStateMap(
                                stories = mergeStep(sender, info.positionTo, info.positionFrom),
                                focusId = parentReceiver.id
                            )
                    }

                    receiver != null -> {
                        _normalizedStepsMap.value =
                            StoryStateMap(
                                stories = mergeStep(sender, info.positionTo, info.positionFrom),
                                focusId = receiver.id
                            )
                    }
                }
            }
    }

    private fun mergeStep(sender: StoryUnit, positionTo: Int, positionFrom: Int): Map<Int, StoryUnit> {
        val mutableHistory = _normalizedStepsMap.value.stories.toEditState()
        val receiverStepList = mutableHistory[positionTo]
        receiverStepList?.plus(sender)?.let { newList ->
            mutableHistory[positionTo] = newList
        }

        if (sender.parentId == null) {
            mutableHistory.remove(positionFrom)
        } else {

        }

        return stepsMapNormalizer(mutableHistory)
    }

    fun moveRequest(moveInfo: MoveInfo) {
        _normalizedStepsMap.value = StoryStateMap(
            stepsMapNormalizer(
                moveHandler.handleMove(
                    _normalizedStepsMap.value.stories, moveInfo
                ).toEditState()
            )
        )

//        _scrollToPosition.value =
//            FindStory.findById(_normalizedStepsMap.value.stories.values, unitId)
//                ?.let { (unit, group) ->
//                    group?.localPosition ?: unit?.localPosition
//                }
    }

    fun checkRequest(checkInfo: CheckInfo) {
        updateState()
        val parentId = checkInfo.storyUnit.parentId

        val storyUnit = if (parentId != null) {
            FindStory.findById(_normalizedStepsMap.value.stories.values, parentId)?.first ?: return
        } else {
            checkInfo.storyUnit
        }

        val newStep = (storyUnit as? StoryStep)?.copy(checked = checkInfo.checked) ?: return
        val newMap = _normalizedStepsMap.value.stories.toMutableMap()

        newMap[newStep.localPosition] = newStep

        _normalizedStepsMap.value = StoryStateMap(newMap)

    }

    fun onTextEdit(text: String, position: Int) {
        textChanges[position] = text
    }

    //Todo: Add unit test for this method!
    fun onLineBreak(storyStep: StoryStep) {
        val updatedStories = updateTexts(_normalizedStepsMap.value.stories)
        _normalizedStepsMap.value = separateMessages(updatedStories.stories, storyStep)
    }

    private fun separateMessages(
        stories: Map<Int, StoryUnit>,
        storyStep: StoryStep
    ): StoryStateMap {
        storyStep.text?.split("\n", limit = 2)?.let { list ->
            val secondText = list.elementAtOrNull(1) ?: ""

            val mutable = stories.values.toMutableList()
            mutable.add(
                storyStep.localPosition + 1,
                StoryStepFactory.space(localPosition = storyStep.localPosition + 1)
            )

            val secondMessage = StoryStep(
                id = UUID.randomUUID().toString(),
                type = storyStep.type,
                text = secondText,
                localPosition = storyStep.localPosition + 2,
            )
            mutable.add(storyStep.localPosition + 2, secondMessage)

            return StoryStateMap(
                stories = stepsMapNormalizer(mutable.associateWithPosition().toEditState()),
                focusId = secondMessage.id
            )
        }

        return StoryStateMap(stories)
    }

    fun updateState() {
        _normalizedStepsMap.value = updateTexts(_normalizedStepsMap.value.stories)
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
        delete(deleteInfo, _normalizedStepsMap.value.stories)
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
            val previousFocus = FindStory.previousFocus(history.values.toList(), step.localPosition)
            val normalized = stepsMapNormalizer(mutableSteps.toEditState())

            _normalizedStepsMap.value = StoryStateMap(normalized, focusId = previousFocus?.id)
        } else {
            FindStory.findById(history.values, parentId)
                ?.first
                ?.let { group ->
                    val newSteps = (group as GroupStep).steps.filter { storyUnit ->
                        storyUnit.id != step.id
                    }

                    val newStoryUnit = if (newSteps.size == 1) {
                        newSteps.first().copyWithNewPosition(group.localPosition)
                    } else {
                        group.copy(steps = newSteps)
                    }

                    mutableSteps[group.localPosition] = newStoryUnit.copyWithNewParent(null)
                    _normalizedStepsMap.value =
                        StoryStateMap(stepsMapNormalizer(mutableSteps.toEditState()))
                }
        }
    }
}

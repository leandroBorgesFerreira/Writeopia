package br.com.leandroferreira.storyteller.manager

import br.com.leandroferreira.storyteller.backstack.BackStackManager
import br.com.leandroferreira.storyteller.backstack.BackstackHandler
import br.com.leandroferreira.storyteller.backstack.BackstackInform
import br.com.leandroferreira.storyteller.model.backtrack.AddStoryUnit
import br.com.leandroferreira.storyteller.model.backtrack.AddText
import br.com.leandroferreira.storyteller.model.change.CheckInfo
import br.com.leandroferreira.storyteller.model.change.DeleteInfo
import br.com.leandroferreira.storyteller.model.change.LineBreakInfo
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.change.MoveInfo
import br.com.leandroferreira.storyteller.model.change.TextEditInfo
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class StoryTellerManager(
    private val stepsNormalizer: UnitsNormalizationMap =
        StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val focusableTypes: Set<String> = setOf(
        StoryType.CHECK_ITEM.type,
        StoryType.MESSAGE.type
    ),
    private val backStackManager: BackStackManager = BackStackManager(),
) : BackstackHandler, BackstackInform by backStackManager {

    private val _scrollToPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val scrollToPosition: StateFlow<Int?> = _scrollToPosition.asStateFlow()

    private val textChanges: MutableMap<Int, String> = mutableMapOf()

    private val _currentStory: MutableStateFlow<StoryState> = MutableStateFlow(
        StoryState(stories = emptyMap())
    )

    val currentStory: StateFlow<StoryState> = _currentStory.asStateFlow()

    fun saveOnStoryChanges(
        coroutineScope: CoroutineScope,
        documentId: String,
        storyStateSaver: StoryStateSaver
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            currentStory.map { storyState ->
                storyState.stories
            }.collectLatest { content ->
                storyStateSaver.saveState(documentId, content)
            }
        }
    }

    fun isInitialized(): Boolean = _currentStory.value.stories.isNotEmpty()

    fun newStory() {
        val firstMessage = StoryStep(
            localId = UUID.randomUUID().toString(),
            type = "message"
        )
        val stories: Map<Int, StoryUnit> = mapOf(0 to firstMessage)

        _currentStory.value = StoryState(stepsNormalizer(stories.toEditState()), firstMessage.id)
    }

    fun initStories(stories: Map<Int, StoryUnit>) {
        _currentStory.value = StoryState(stepsNormalizer(stories.toEditState()), null)
    }

    fun mergeRequest(info: MergeInfo) {
        val sender = info.sender
        val receiver = info.receiver
        val positionTo = info.positionTo
        val positionFrom = info.positionFrom

        if (info.positionFrom == info.positionTo) return

        val mutableHistory = _currentStory.value.stories.toEditState()
        val receiverStepList = mutableHistory[positionTo]
        receiverStepList?.plus(sender.copyWithNewParent(receiver.parentId))?.let { newList ->
            mutableHistory[positionTo] = newList
        }

        if (sender.parentId == null) {
            mutableHistory.remove(positionFrom)
        } else {
            val fromGroup = (mutableHistory[positionFrom]?.first() as? GroupStep)
            val newList =
                fromGroup?.steps?.filter { storyUnit -> storyUnit.localId != sender.localId }

            if (newList != null) {
                mutableHistory[positionFrom] = listOf(fromGroup.copy(steps = newList))
            }
        }

        _currentStory.value = StoryState(stories = stepsNormalizer(mutableHistory))
    }

    fun moveRequest(moveInfo: MoveInfo) {
        val mutable = _currentStory.value.stories.toMutableMap()
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

        _currentStory.value = StoryState(stepsNormalizer(mutable.toEditState()))
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     */
    fun checkRequest(checkInfo: CheckInfo) {
        updateState()
        val storyUnit = checkInfo.storyUnit

        val newStep = (storyUnit as? StoryStep)?.copy(checked = checkInfo.checked) ?: return
        val newMap = _currentStory.value.stories.toMutableMap()

        newMap[checkInfo.position] = newStep

        _currentStory.value = StoryState(newMap)
    }

    fun onTextEdit(text: String, position: Int) {
        textChanges[position] = text
        backStackManager.addAction(TextEditInfo(text, position))
    }

    fun onLineBreak(lineBreakInfo: LineBreakInfo) {
        val updatedStories = updateTexts(_currentStory.value.stories)
        val (position, newContent: StoryState) = separateMessages(
            updatedStories.stories,
            lineBreakInfo
        )

        _currentStory.value = newContent
        _scrollToPosition.value = position
    }

    fun updateState() {
        _currentStory.value = updateTexts(_currentStory.value.stories)
    }

    override fun undo() {
        when (val backAction = backStackManager.undo()) {
            is DeleteInfo -> {
                _currentStory.value = revertDelete(backAction)
            }

            is AddStoryUnit -> {
                revertAddStory(backAction)
            }

            is AddText -> {
                revertAddText(currentStory.value.stories, backAction)
            }

            else -> return
        }
    }


    override fun redo() {
        when (val action = backStackManager.redo()) {
            is DeleteInfo -> {
                delete(action, currentStory.value.stories)
            }

            is AddStoryUnit -> {
                val (position, newStory) = addNewContent(
                    currentStory.value.stories,
                    action.storyUnit,
                    action.position - 1
                )
                _currentStory.value = StoryState(
                    newStory,
                    newStory[position]?.id
                )

                _scrollToPosition.value = position
            }

            is AddText -> {
                redoAddText(currentStory.value.stories, action)
            }

            else -> return
        }
    }

    private fun revertAddText(currentStory: Map<Int, StoryUnit>, addText: AddText) {
        val mutableSteps = currentStory.toMutableMap()
        //Todo: Merging StoryStep and StoryGroups could reduce casts
        val revertStep = mutableSteps[addText.position] as StoryStep
        val currentText = revertStep.text

        if (!currentText.isNullOrEmpty()) {
            val newText = if (currentText.length <= addText.text.length) {
                ""
            } else {
                currentText.substring(currentText.length - addText.text.length)
            }

            mutableSteps[addText.position] =
                revertStep.copy(localId = UUID.randomUUID().toString(), text = newText)

            _currentStory.value = StoryState(mutableSteps, focusId = revertStep.id)
        }
    }

    private fun redoAddText(currentStory: Map<Int, StoryUnit>, addText: AddText) {
        val position = addText.position
        val mutableSteps = currentStory.toMutableMap()
        val editStep = mutableSteps[position] as StoryStep
        val newText = "${editStep.text.toString()}${addText.text}"

        mutableSteps[position] = editStep.copy(text = newText)

        _currentStory.value = StoryState(mutableSteps, focusId = editStep.id)
    }

    private fun revertAddStory(addStoryUnit: AddStoryUnit) {
        delete(
            DeleteInfo(addStoryUnit.storyUnit, position = addStoryUnit.position),
            currentStory.value.stories
        )
    }

    private fun revertDelete(deleteInfo: DeleteInfo): StoryState {
        val (_, newStory) = addNewContent(
            currentStory.value.stories,
            deleteInfo.storyUnit,
            deleteInfo.position
        )

        return StoryState(
            stories = newStory,
            focusId = deleteInfo.storyUnit.id
        )
    }

    private fun separateMessages(
        stories: Map<Int, StoryUnit>,
        lineBreakInfo: LineBreakInfo
    ): Pair<Int?, StoryState> {
        val storyStep = lineBreakInfo.storyStep
        storyStep.text?.split("\n", limit = 2)?.let { list ->
            val secondText = list.elementAtOrNull(1) ?: ""
            val secondMessage = StoryStep(
                localId = UUID.randomUUID().toString(),
                type = storyStep.type,
                text = secondText,
            )

            val position = lineBreakInfo.position + 1

            val (addedPosition, newStory) = addNewContent(stories, secondMessage, position)
            backStackManager.addAction(AddStoryUnit(secondMessage, position = addedPosition))

            return addedPosition to StoryState(
                stories = newStory,
                focusId = secondMessage.id
            )
        }

        return null to StoryState(stories)
    }

    private fun addNewContent(
        currentStory: Map<Int, StoryUnit>,
        newStoryUnit: StoryUnit,
        position: Int
    ): Pair<Int, Map<Int, StoryUnit>> {
        val mutable = currentStory.values.toMutableList()
        var acc = position

        mutable.add(acc++, StoryStepFactory.space())
        mutable.add(acc, newStoryUnit)

        return acc to mutable.associateWithPosition()
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
        val newSteps = updateTexts(_currentStory.value.stories)

        delete(deleteInfo, newSteps.stories)
        backStackManager.addAction(deleteInfo)
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
                FindStory.previousFocus(
                    history.values.toList(),
                    deleteInfo.position,
                    focusableTypes
                )
            val normalized = stepsNormalizer(mutableSteps.toEditState())

            _currentStory.value = StoryState(
                normalized,
                focusId = previousFocus?.id
            )
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
                _currentStory.value =
                    StoryState(stepsNormalizer(mutableSteps.toEditState()))
            }
        }
    }
}

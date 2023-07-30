package com.github.leandroborgesferreira.storyteller.manager

import android.util.Log
import com.github.leandroborgesferreira.storyteller.backstack.BackstackHandler
import com.github.leandroborgesferreira.storyteller.backstack.BackstackInform
import com.github.leandroborgesferreira.storyteller.backstack.BackstackManager
import com.github.leandroborgesferreira.storyteller.backstack.PerStateBackstackManager
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.model.story.Decoration
import com.github.leandroborgesferreira.storyteller.model.story.DrawState
import com.github.leandroborgesferreira.storyteller.model.story.DrawStory
import com.github.leandroborgesferreira.storyteller.model.story.LastEdit
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.normalization.builder.StepsMapNormalizationBuilder
import com.github.leandroborgesferreira.storyteller.utils.alias.UnitsNormalizationMap
import com.github.leandroborgesferreira.storyteller.utils.extensions.toBackStack
import com.github.leandroborgesferreira.storyteller.utils.extensions.toEditState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class StoryTellerManager(
    private val stepsNormalizer: UnitsNormalizationMap =
        StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val movementHandler: MovementHandler = MovementHandler(),
    private val contentHandler: ContentHandler = ContentHandler(
        focusableTypes = setOf(
            StoryType.CHECK_ITEM.type,
            StoryType.MESSAGE.type,
            StoryType.MESSAGE_BOX.type,
        ),
        stepsNormalizer = stepsNormalizer
    ),
    private val backStackManager: BackstackManager = PerStateBackstackManager(
        contentHandler = contentHandler
    ),
    private val focusHandler: FocusHandler = FocusHandler(),
    private val coroutineScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    ),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BackstackHandler, BackstackInform by backStackManager {

    private val _scrollToPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val scrollToPosition: StateFlow<Int?> = _scrollToPosition.asStateFlow()

    private val _currentStory: MutableStateFlow<StoryState> = MutableStateFlow(
        StoryState(stories = emptyMap(), lastEdit = LastEdit.Nothing)
    )

    private val _documentInfo: MutableStateFlow<DocumentInfo> = MutableStateFlow(DocumentInfo())

    private val _positionsOnEdit = MutableStateFlow(setOf<Int>())
    val onEditPositions = _positionsOnEdit.asStateFlow()

    val currentStory: StateFlow<StoryState> = _currentStory.asStateFlow()

    val currentDocument: Flow<Document> = combine(_documentInfo, _currentStory) { info, state ->
        val titleFromContent = state.stories.values.firstOrNull { storyStep ->
            //Todo: Change the type of change to allow different types. The client code should decide what is a title
            //It is also interesting to inv
            storyStep.type == StoryType.TITLE.type
        }?.text

        Document(
            id = info.id,
            title = titleFromContent ?: info.title,
            content = state.stories,
            createdAt = info.createdAt,
            lastUpdatedAt = info.lastUpdatedAt,
        )
    }

    private val _documentEditionState: Flow<Pair<StoryState, DocumentInfo>> =
        combine(currentStory, _documentInfo) { storyState, documentInfo ->
            storyState to documentInfo
        }

    val toDraw = combine(_positionsOnEdit, currentStory) { positions, storyState ->
        val focus = storyState.focusId

        val toDrawStories = storyState.stories.mapValues { (position, storyStep) ->
            DrawStory(storyStep, positions.contains(position))
        }

        DrawState(toDrawStories, focus)
    }

    private val isOnSelection: Boolean
        get() = _positionsOnEdit.value.isNotEmpty()


    //Todo: Evaluate if this should be extract to a specific class
    fun saveOnStoryChanges(documentUpdate: DocumentUpdate) {
        coroutineScope.launch(dispatcher) {
            _documentEditionState.collectLatest { (storyState, documentInfo) ->
                when (val lastEdit = storyState.lastEdit) {
                    is LastEdit.LineEdition -> {
                        documentUpdate.saveStoryStep(
                            storyStep = lastEdit.storyStep.copy(
                                localId = UUID.randomUUID().toString()
                            ),
                            position = lastEdit.position,
                            documentId = documentInfo.id
                        )
                    }

                    LastEdit.Nothing -> {}

                    LastEdit.Whole -> {
                        val stories = storyState.stories
                        val titleFromContent = stories.values.firstOrNull { storyStep ->
                            //Todo: Change the type of change to allow different types. The client code should decide what is a title
                            //It is also interesting to inv
                            storyStep.type == StoryType.TITLE.type
                        }?.text

                        val document = Document(
                            id = documentInfo.id,
                            title = titleFromContent ?: documentInfo.title,
                            content = storyState.stories,
                            createdAt = documentInfo.createdAt,
                            lastUpdatedAt = documentInfo.lastUpdatedAt,
                        )

                        documentUpdate.saveDocument(document)
                    }

                    is LastEdit.InfoEdition -> {
                        val stories = storyState.stories
                        val titleFromContent = stories.values.firstOrNull { storyStep ->
                            //Todo: Change the type of change to allow different types. The client code should decide what is a title
                            //It is also interesting to inv
                            storyStep.type == StoryType.TITLE.type
                        }?.text

                        documentUpdate.saveDocumentMetadata(
                            Document(
                                id = documentInfo.id,
                                title = titleFromContent ?: documentInfo.title,
                                createdAt = documentInfo.createdAt,
                                lastUpdatedAt = documentInfo.lastUpdatedAt,
                            )
                        )

                        documentUpdate.saveStoryStep(
                            storyStep = lastEdit.storyStep,
                            position = lastEdit.position,
                            documentId = documentInfo.id
                        )
                    }
                }
            }
        }
    }

    fun isInitialized(): Boolean = _currentStory.value.stories.isNotEmpty()

    fun newStory(documentId: String = UUID.randomUUID().toString(), title: String = "") {
        val firstMessage = StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryType.TITLE.type
        )
        val stories: Map<Int, StoryStep> = mapOf(0 to firstMessage)
        val normalized = stepsNormalizer(stories.toEditState())

        _documentInfo.value = DocumentInfo(
            id = documentId,
            title = title,
            createdAt = Date(),
            lastUpdatedAt = Date()
        )

        _currentStory.value = StoryState(
            normalized + normalized,
            LastEdit.Nothing,
            firstMessage.id
        )
    }

    //Todo: Add unit test fot this!!
    fun nextFocusOrCreate(position: Int) {
        coroutineScope.launch(dispatcher) {
            val storyMap = _currentStory.value.stories
            val nextFocus = focusHandler.findNextFocus(position, _currentStory.value.stories)
            if (nextFocus != null) {
                val (nextPosition, storyStep) = nextFocus
                val mutable = storyMap.toMutableMap()
                mutable[nextPosition] = storyStep.copy(localId = UUID.randomUUID().toString())
                _currentStory.value =
                    _currentStory.value.copy(stories = mutable, focusId = storyStep.id)
            }
        }
    }

    fun initDocument(document: Document) {
        if (isInitialized()) return

        val stories = document.content ?: emptyMap()
        _currentStory.value =
            StoryState(stepsNormalizer(stories.toEditState()), LastEdit.Nothing, null)
        val normalized = stepsNormalizer(stories.toEditState())

        _currentStory.value = StoryState(normalized, LastEdit.Nothing)
        _documentInfo.value = document.info()
    }

    fun mergeRequest(info: Action.Merge) {
        if (isOnSelection) {
            cancelSelection()
        }

        val movedStories = movementHandler.merge(_currentStory.value.stories, info)
        _currentStory.value = StoryState(
            stories = stepsNormalizer(movedStories),
            lastEdit = LastEdit.Whole
        )
    }

    fun moveRequest(moveInfo: Action.Move) {
        if (isOnSelection) {
            cancelSelection()
        }

        val newStory = movementHandler.move(_currentStory.value.stories, moveInfo)
        _currentStory.value = StoryState(
            stepsNormalizer(newStory.toEditState()),
            lastEdit = LastEdit.Whole
        )
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     */
    fun checkRequest(check: Action.Check) {
        if (isOnSelection) {
            cancelSelection()
        }

        val storyUnit = check.storyStep
        val newStep = (storyUnit as? StoryStep)?.copy(checked = check.checked) ?: return

        contentHandler.changeStoryStepState(
            _currentStory.value.stories,
            newStep,
            check.position
        )?.let { state ->
            _currentStory.value = state
            backStackManager.addAction(check.toBackStack())
        }
    }

    fun createCheckItem(position: Int) {
        if (isOnSelection) {
            cancelSelection()
        }

        _currentStory.value = contentHandler.createCheckItem(_currentStory.value.stories, position)
    }

    fun onTextEdit(text: String, position: Int) {
        if (isOnSelection) {
            cancelSelection()
        }

        Log.d("Manager", "onTextEdit")

        val currentStory = _currentStory.value.stories
        val oldText = _currentStory.value.stories[position]?.text
        val newStory = _currentStory.value.stories[position]?.copy(text = text)

        if (newStory != null && oldText != text) {
            Log.d("Manager", "Adding story")

            contentHandler.changeStoryStepState(currentStory, newStory, position)
                ?.let { newState ->
                    _currentStory.value = newState
                    backStackManager.addAction(BackstackAction.StoryTextChange(newStory, position))
                }
        }
    }

    fun onTitleEdit(text: String, position: Int) {
        if (isOnSelection) {
            cancelSelection()
        }

        _currentStory.value.stories[position]?.copy(text = text)?.let { newStory ->
            val newMap = _currentStory.value.stories.toMutableMap()
            newMap[position] = newStory
            _currentStory.value = StoryState(newMap, LastEdit.InfoEdition(position, newStory))
            backStackManager.addAction(BackstackAction.StoryStateChange(newStory, position))
        }
    }

    fun headerColorSelection(color: Int?) {
        if (isOnSelection) {
            cancelSelection()
        }

        val currentStory = _currentStory.value.stories
        val newStory =
            _currentStory.value.stories[0]?.copy(decoration = Decoration(backgroundColor = color))

        if (newStory != null) {
            contentHandler.changeStoryStepState(currentStory, newStory, 0)?.let { newState ->
                _currentStory.value = newState
            }
        }
    }

    fun onLineBreak(lineBreak: Action.LineBreak) {
        if (isOnSelection) {
            cancelSelection()
        }

        coroutineScope.launch(dispatcher) {
            contentHandler.onLineBreak(_currentStory.value.stories, lineBreak)
                ?.let { (info, newState) ->
                    // Todo: Fix this when the inner position are completed
                    backStackManager.addAction(lineBreak.toBackStack())

                    _currentStory.value = newState
                    _scrollToPosition.value = info.first
                }
        }
    }

    fun onSelected(isSelected: Boolean, position: Int) {
        coroutineScope.launch(dispatcher) {
            if (_currentStory.value.stories[position] != null) {
                val newOnEdit = if (isSelected) {
                    _positionsOnEdit.value + position
                } else {
                    _positionsOnEdit.value - position
                }
                _positionsOnEdit.value = newOnEdit
            }
        }
    }

    fun clickAtTheEnd() {
        val stories = _currentStory.value.stories
        val lastContentStory = stories[stories.size - 3]

        if (lastContentStory?.type == StoryType.MESSAGE.type) {
            val newState = _currentStory.value.copy(focusId = lastContentStory.id)
            _currentStory.value = newState
        } else {
            var acc = stories.size - 1
            val newLastMessage = StoryStep(type = StoryType.MESSAGE.type)

            //Todo: It should be possible to customize which steps are add
            val newStories = stories + mapOf(
                acc++ to newLastMessage,
                acc++ to StoryStep(type = StoryType.SPACE.type),
                acc to StoryStep(type = StoryType.LARGE_SPACE.type),
            )

            _currentStory.value = StoryState(newStories, LastEdit.Whole, newLastMessage.id)
        }
    }

    override fun undo() {
        _currentStory.value = backStackManager.previousState(currentStory.value)
    }


    override fun redo() {
        _currentStory.value = backStackManager.previousState(currentStory.value)
    }


    fun onDelete(deleteStory: Action.DeleteStory) {
        coroutineScope.launch(dispatcher) {
            contentHandler.deleteStory(deleteStory, _currentStory.value.stories)?.let { newState ->
                _currentStory.value = newState
            }

            backStackManager.addAction(deleteStory.toBackStack())
        }
    }

    fun deleteSelection() {
        coroutineScope.launch(dispatcher) {
            val (newStories, deletedStories) = contentHandler.bulkDeletion(
                _positionsOnEdit.value,
                _currentStory.value.stories
            )

            backStackManager.addAction(BackstackAction.BulkDelete(deletedStories))
            _positionsOnEdit.value = emptySet()

            _currentStory.value =
                _currentStory.value.copy(stories = newStories)
        }
    }

    private fun revertAddText(currentStory: Map<Int, StoryStep>, addText: Action.AddText) {
        val mutableSteps = currentStory.toMutableMap()
        val revertStep = currentStory[addText.position]
        val currentText = revertStep?.text

        if (!currentText.isNullOrEmpty()) {
            val newText = if (currentText.length <= addText.text.length) {
                ""
            } else {
                currentText.substring(currentText.length - addText.text.length)
            }

            mutableSteps[addText.position] =
                revertStep.copy(localId = UUID.randomUUID().toString(), text = newText)

            _currentStory.value = StoryState(
                mutableSteps,
                lastEdit = LastEdit.Whole,
                focusId = revertStep.id
            )
        }
    }

    private fun redoAddText(currentStory: Map<Int, StoryStep>, addText: Action.AddText) {
        val position = addText.position
        val mutableSteps = currentStory.toMutableMap()
        mutableSteps[position]?.let { editStep ->
            val newText = "${editStep.text.toString()}${addText.text}"
            mutableSteps[position] = editStep.copy(text = newText)
            _currentStory.value = StoryState(
                mutableSteps,
                lastEdit = LastEdit.Whole,
                focusId = editStep.id
            )
        }
    }

    private fun revertAddStory(addStoryUnit: Action.AddStory) {
        coroutineScope.launch(dispatcher) {
            contentHandler.deleteStory(
                Action.DeleteStory(addStoryUnit.storyStep, position = addStoryUnit.position),
                _currentStory.value.stories
            )?.let { newState ->
                _currentStory.value = newState
            }
        }
    }



    private fun cancelSelection() {
        _positionsOnEdit.value = emptySet()
    }

}

/**
 * Dto class to keep information about the document
 */
data class DocumentInfo(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val createdAt: Date = Date(),
    val lastUpdatedAt: Date = Date(),
)

fun Document.info(): DocumentInfo = DocumentInfo(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    lastUpdatedAt = this.lastUpdatedAt,
)
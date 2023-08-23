package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.backstack.BackstackHandler
import com.github.leandroborgesferreira.storyteller.backstack.BackstackInform
import com.github.leandroborgesferreira.storyteller.backstack.BackstackManager
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction
import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.model.document.DocumentInfo
import com.github.leandroborgesferreira.storyteller.model.document.info
import com.github.leandroborgesferreira.storyteller.model.story.DrawState
import com.github.leandroborgesferreira.storyteller.model.story.DrawStory
import com.github.leandroborgesferreira.storyteller.model.story.LastEdit
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.normalization.builder.StepsMapNormalizationBuilder
import com.github.leandroborgesferreira.storyteller.utils.alias.UnitsNormalizationMap
import com.github.leandroborgesferreira.storyteller.utils.extensions.toEditState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

/**
 * This is the entry class of the framework. It follows the Controller pattern, redirecting all the
 * call to another class responsible for the part of the SDK requested. 
 */
class StoryTellerManager(
    private val stepsNormalizer: UnitsNormalizationMap =
        StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val movementHandler: MovementHandler = MovementHandler(stepsNormalizer),
    private val contentHandler: ContentHandler = ContentHandler(
        focusableTypes = setOf(
            StoryTypes.CHECK_ITEM.type.number,
            StoryTypes.MESSAGE.type.number,
            StoryTypes.MESSAGE_BOX.type.number,
        ),
        stepsNormalizer = stepsNormalizer
    ),
    private val focusHandler: FocusHandler = FocusHandler(),
    private val coroutineScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    ),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val backStackManager: BackstackManager = BackstackManager.create(
        contentHandler,
        movementHandler
    ),
    private val documentTracker: DocumentTracker? = null
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

    val currentDocument: Flow<Document?> = combine(_documentInfo, _currentStory) { info, state ->
        val titleFromContent = state.stories.values.firstOrNull { storyStep ->
            //Todo: Change the type of change to allow different types. The client code should decide what is a title
            //It is also interesting to inv
            storyStep.type == StoryTypes.TITLE.type
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
    fun saveOnStoryChanges() {
        coroutineScope.launch(dispatcher) {
            if (documentTracker != null) {
                documentTracker.saveOnStoryChanges(_documentEditionState)
            } else {
                throw IllegalStateException(
                    "saveOnStoryChanges called without providing a DocumentTracker for " +
                            "StoryTellerManager. Did you forget to add it in the constructor of " +
                            "StoryTellerManager?"
                )
            }
        }
    }

    fun isInitialized(): Boolean = _currentStory.value.stories.isNotEmpty()

    fun newStory(documentId: String = UUID.randomUUID().toString(), title: String = "") {
        val firstMessage = StoryStep(
            localId = UUID.randomUUID().toString(),
            type = StoryTypes.TITLE.type
        )
        val stories: Map<Int, StoryStep> = mapOf(0 to firstMessage)
        val normalized = stepsNormalizer(stories.toEditState())

        _documentInfo.value = DocumentInfo(
            id = documentId,
            title = title,
            createdAt = Instant.now(),
            lastUpdatedAt = Instant.now()
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

    fun moveRequest(move: Action.Move) {
        if (isOnSelection) {
            cancelSelection()
        }

        val newStory = movementHandler.move(_currentStory.value.stories, move)
        _currentStory.value = StoryState(newStory, lastEdit = LastEdit.Whole)

        val backStackAction = BackstackAction.Move(
            storyStep = move.storyStep,
            positionFrom = move.positionFrom,
            positionTo = move.positionTo
        )
        backStackManager.addAction(backStackAction)
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     */
    fun changeStoryState(stateChange: Action.StoryStateChange) {
        if (isOnSelection) {
            cancelSelection()
        }

        val oldStory = _currentStory.value.stories[stateChange.position] ?: return

        contentHandler.changeStoryStepState(
            _currentStory.value.stories,
            stateChange.storyStep,
            stateChange.position
        )?.let { state ->
            _currentStory.value = state
            backStackManager.addAction(
                BackstackAction.StoryStateChange(
                    storyStep = oldStory,
                    position = stateChange.position
                )
            )
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

        val currentStory = _currentStory.value.stories
        val oldText = _currentStory.value.stories[position]?.text
        val newStory = _currentStory.value.stories[position]?.copy(text = text)

        if (newStory != null && oldText != text) {
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

    fun onLineBreak(lineBreak: Action.LineBreak) {
        if (isOnSelection) {
            cancelSelection()
        }

        coroutineScope.launch(dispatcher) {
            contentHandler.onLineBreak(_currentStory.value.stories, lineBreak)
                ?.let { (info, newState) ->
                    val (newPosition, newStory) = info
                    // Todo: Fix this when the inner position are completed
                    backStackManager.addAction(BackstackAction.Add(newStory, newPosition))
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

        if (lastContentStory?.type == StoryTypes.MESSAGE.type) {
            val newState = _currentStory.value.copy(focusId = lastContentStory.id)
            _currentStory.value = newState
        } else {
            var acc = stories.size - 1
            val newLastMessage =
                StoryStep(type = StoryTypes.MESSAGE.type)

            //Todo: It should be possible to customize which steps are add
            val newStories = stories + mapOf(
                acc++ to newLastMessage,
                acc++ to StoryStep(type = StoryTypes.SPACE.type),
                acc to StoryStep(type = StoryTypes.LARGE_SPACE.type),
            )

            _currentStory.value = StoryState(newStories, LastEdit.Whole, newLastMessage.id)
        }
    }

    override fun undo() {
        coroutineScope.launch(dispatcher) {
            cancelSelection()

            _currentStory.value = backStackManager.previousState(currentStory.value)
        }
    }


    override fun redo() {
        coroutineScope.launch(dispatcher) {
            cancelSelection()
            _currentStory.value = backStackManager.nextState(currentStory.value)
        }
    }

    fun onDelete(deleteStory: Action.DeleteStory) {
        coroutineScope.launch(dispatcher) {
            contentHandler.deleteStory(deleteStory, _currentStory.value.stories)?.let { newState ->
                _currentStory.value = newState
            }

            val backstackAction = BackstackAction.Delete(
                storyStep = deleteStory.storyStep,
                position = deleteStory.position
            )

            backStackManager.addAction(backstackAction)
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

    private fun cancelSelection() {
        _positionsOnEdit.value = emptySet()
    }

    fun onClear() {
        coroutineScope.cancel()
    }
}

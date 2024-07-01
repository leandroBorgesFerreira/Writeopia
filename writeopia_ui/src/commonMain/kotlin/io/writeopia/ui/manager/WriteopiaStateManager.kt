package io.writeopia.ui.manager

import io.writeopia.sdk.manager.ContentHandler
import io.writeopia.sdk.manager.DocumentTracker
import io.writeopia.sdk.manager.MovementHandler
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.manager.fixMove
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.action.BackstackAction
import io.writeopia.sdk.model.document.DocumentInfo
import io.writeopia.sdk.model.document.info
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.model.story.DrawStory
import io.writeopia.sdk.model.story.LastEdit
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.command.TypeInfo
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.normalization.builder.StepsMapNormalizationBuilder
import io.writeopia.sdk.sharededition.SharedEditionManager
import io.writeopia.sdk.utils.alias.UnitsNormalizationMap
import io.writeopia.sdk.utils.extensions.toEditState
import io.writeopia.ui.backstack.BackstackHandler
import io.writeopia.ui.backstack.BackstackInform
import io.writeopia.ui.backstack.BackstackManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * This is the entry class of the framework. It follows the Controller pattern, redirecting all the
 * call to another class responsible for the part of the SDK requested.
 */
class WriteopiaStateManager(
    private val stepsNormalizer: UnitsNormalizationMap =
        StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    private val backStackManager: BackstackManager,
    private val userId: suspend () -> String = { "no_user_id_provided" },
    private val writeopiaManager: WriteopiaManager
) : BackstackHandler, BackstackInform by backStackManager {

    private var lastStateChange: Action.StoryStateChange? = null

    private val initialContent: Map<Int, StoryStep> =
        mapOf(0 to StoryStep(text = "", type = StoryTypes.TITLE.type))

    private var localUserId: String? = null

    private val _scrollToPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val scrollToPosition: StateFlow<Int?> = _scrollToPosition.asStateFlow()

    private val _currentStory: MutableStateFlow<StoryState> = MutableStateFlow(
        StoryState(stories = initialContent, lastEdit = LastEdit.Nothing)
    )

    private val _documentInfo: MutableStateFlow<DocumentInfo> =
        MutableStateFlow(DocumentInfo.empty())

    private val _positionsOnEdit = MutableStateFlow(setOf<Int>())
    val onEditPositions = _positionsOnEdit.asStateFlow()

    private var sharedEditionManager: SharedEditionManager? = null

    val currentStory: StateFlow<StoryState> = _currentStory.asStateFlow()

    val currentDocument: Flow<Document?> = combine(_documentInfo, _currentStory) { info, state ->
        val titleFromContent = state.stories.values.firstOrNull { storyStep ->
            // Todo: Change the type of change to allow different types. The client code should decide what is a title
            // It is also interesting to inv
            storyStep.type == StoryTypes.TITLE.type
        }?.text

        Document(
            id = info.id,
            title = titleFromContent ?: info.title,
            content = state.stories,
            createdAt = info.createdAt,
            lastUpdatedAt = info.lastUpdatedAt,
            userId = localUserId ?: userId.invoke().also { id ->
                localUserId = id
            },
            parentId = info.parentId
        )
    }

    private val _documentEditionState: Flow<Pair<StoryState, DocumentInfo>> =
        combine(currentStory, _documentInfo) { storyState, documentInfo ->
            storyState to documentInfo
        }

    val toDraw: Flow<DrawState> = combine(_positionsOnEdit, currentStory) { positions, storyState ->
        val focus = storyState.focusId

        val toDrawStories = storyState.stories.mapValues { (position, storyStep) ->
            DrawStory(storyStep, positions.contains(position))
        }

        DrawState(toDrawStories, focus)
    }

    private var _initialized = false

    private suspend fun getUserId(): String =
        localUserId ?: userId.invoke().also { id ->
            localUserId = id
        }

    private val isOnSelection: Boolean
        get() = _positionsOnEdit.value.isNotEmpty()

    /**
     * Saves the document automatically as it is changed. It uses the [DocumentTracker] passed
     * in the constructor of [WriteopiaStateManager]
     */
    fun saveOnStoryChanges(documentTracker: DocumentTracker) {
        coroutineScope.launch(dispatcher) {
            documentTracker.saveOnStoryChanges(_documentEditionState, getUserId())
        }
    }

    fun liveSync(sharedEditionManager: SharedEditionManager) {
        coroutineScope.launch(dispatcher) {
            sharedEditionManager.startLiveEdition(
                inFlow = _documentEditionState,
                outFlow = _currentStory,
            )
        }
    }

    fun isInitialized(): Boolean = _initialized

    /**
     * Creates a new story. Use this when you wouldn't like to load a documented previously saved.
     *
     * @param documentId the id of the document that will be created
     * @param title the title of the document
     */
    fun newStory(
        documentId: String = GenerateId.generate(),
        title: String = "",
        forceRestart: Boolean = false
    ) {
        if (isInitialized() && !forceRestart) return

        _initialized = true
        val (documentInfo, storyState) = writeopiaManager.newStory(documentId, title)

        _documentInfo.value = documentInfo
        _currentStory.value = storyState
    }

    /**
     * Initializes a document passed as a parameter. This method should be used when you would like
     * to load a document from a database and start editing it, instead of creating something new.
     *
     * @param document [Document]
     */
    fun loadDocument(document: Document) {
        if (isInitialized()) return

        _initialized = true

        val stories = document.content
        _currentStory.value =
            StoryState(stepsNormalizer(stories.toEditState()), LastEdit.Nothing, null)
        val normalized = stepsNormalizer(stories.toEditState())

        _currentStory.value = StoryState(normalized, LastEdit.Nothing)
        _documentInfo.value = document.info()
    }

    /**
     * Moves the focus to the next available [StoryStep] if it can't find a step to focus, it
     * creates a new [StoryStep] at the end of the document.
     *
     * @param position Int
     */
    // Todo: Add unit tests
    fun nextFocusOrCreate(position: Int) {
        coroutineScope.launch(dispatcher) {
            _currentStory.value =
                writeopiaManager.nextFocusOrCreate(position, _currentStory.value)
        }
    }

    /**
     * Merges two [StoryStep] into a group. This can be used to merge two images into a message
     * group or any other kind of group.
     *
     * @param info [Action.Merge]
     */
    fun mergeRequest(info: Action.Merge) {
        if (isOnSelection) {
            cancelSelection()
        }

        _currentStory.value = writeopiaManager.mergeRequest(info, _currentStory.value)

        // Todo: Add to backstack
    }

    /**
     * A request to move a content to a position.
     *
     * @param move [Action.Move]
     */
    fun moveRequest(move: Action.Move) {
        if (isOnSelection) {
            cancelSelection()
        }

        val fixedMove = move.fixMove()

        _currentStory.value = writeopiaManager.moveRequest(fixedMove, _currentStory.value)

        val backStackAction = BackstackAction.Move(
            storyStep = fixedMove.storyStep,
            positionFrom = fixedMove.positionFrom,
            positionTo = fixedMove.positionTo
        )
        backStackManager.addAction(backStackAction)
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     *
     * @param stateChange [Action.StoryStateChange]
     */
    fun changeStoryState(stateChange: Action.StoryStateChange) {
        val backstackAction = _currentStory.value.stories[stateChange.position]?.let { oldStory ->
            BackstackAction.StoryStateChange(
                storyStep = oldStory,
                position = stateChange.position
            )
        }

        changeStoryStateAndTrackIt(stateChange, backstackAction)
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     *
     * @param stateChange [Action.StoryStateChange]
     */
    fun changeStoryText(stateChange: Action.StoryStateChange) {
        val backstackAction = _currentStory.value.stories[stateChange.position]?.let { oldStory ->
            BackstackAction.StoryTextChange(
                storyStep = oldStory,
                position = stateChange.position
            )
        }

        changeStoryStateAndTrackIt(stateChange, backstackAction)
    }

    private fun changeStoryStateAndTrackIt(
        stateChange: Action.StoryStateChange,
        backstackAction: BackstackAction?
    ) {
        if (lastStateChange == stateChange) return
        lastStateChange = stateChange

        if (isOnSelection) {
            cancelSelection()
        }

        writeopiaManager.changeStoryState(stateChange, _currentStory.value)?.let { state ->
            _currentStory.value = state
            backstackAction?.let { action ->
                backStackManager.addAction(action)
            }
        }
    }

    /**
     * Changes the story type. The type of a messages changes without changing the content of it.
     * Commands normally change the type of a message. From a message to a unordered list item, for
     * example.
     *
     * @param position Int
     * @param storyType [StoryStep]
     * @param commandInfo [CommandInfo]
     */
    fun changeStoryType(position: Int, typeInfo: TypeInfo, commandInfo: CommandInfo?) {
        if (isOnSelection) {
            cancelSelection()
        }

        _currentStory.value =
            writeopiaManager.changeStoryType(position, typeInfo, commandInfo, _currentStory.value)
    }

    /**
     * Creates a line break. When a line break happens, the line it divided into two [StoryStep]s
     * of the same, if possible, or the next line will be a Message.
     *
     * @param lineBreak [Action.LineBreak]
     */
    fun onLineBreak(lineBreak: Action.LineBreak) {
        if (isOnSelection) {
            cancelSelection()
        }

        coroutineScope.launch(dispatcher) {
            val state = _currentStory.value
            writeopiaManager.onLineBreak(lineBreak, state)?.let { (info, newState) ->
                val (newPosition, newStory) = info
                // Todo: Fix this when the inner position are completed
                backStackManager.addAction(BackstackAction.Add(newStory, newPosition))
                _currentStory.value = newState
                _scrollToPosition.value = info.first
            }
        }
    }

    /**
     * Add a [StoryStep] of a position into the selection list. Selected content can be used to
     * perform bulk actions, like bulk edition and bulk deletion.
     */
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

    /**
     * A click at the end of the document. The focus should be moved to the first [StoryStep] that
     * can receive the focus, from last to first.
     */
    fun clickAtTheEnd() {
        val stories = _currentStory.value.stories
        val lastPosition = stories.size - 1
        val lastContentStory = stories[lastPosition]

        val newState = if (lastContentStory?.type == StoryTypes.TEXT.type) {
            val newStoriesState = stories.toMutableMap().apply {
                this[lastPosition] = lastContentStory.copyNewLocalId()
            }

            _currentStory.value.copy(focusId = lastContentStory.id, stories = newStoriesState)
        } else {
            val newLastMessage = StoryStep(type = StoryTypes.TEXT.type)
            val newStories = stories + mapOf(stories.size to newLastMessage)

            StoryState(newStories, LastEdit.Whole, newLastMessage.id)
        }

        _currentStory.value = newState
    }

    /**
     * Undo the last action.
     */
    override fun undo() {
        coroutineScope.launch(dispatcher) {
            cancelSelection()

            _currentStory.value = backStackManager.previousState(currentStory.value)
        }
    }

    /**
     * Redo the last undone action.
     */
    override fun redo() {
        coroutineScope.launch(dispatcher) {
            cancelSelection()
            _currentStory.value = backStackManager.nextState(currentStory.value)
        }
    }

    /**
     * Deletes a [StoryStep]
     *
     * @param deleteStory [Action.DeleteStory]
     */
    fun onDelete(deleteStory: Action.DeleteStory) {
        coroutineScope.launch(dispatcher) {
            writeopiaManager.onDelete(deleteStory, _currentStory.value)?.let { newState ->
                _currentStory.value = newState
            }

            val backstackAction = BackstackAction.Delete(
                storyStep = deleteStory.storyStep,
                position = deleteStory.position
            )

            backStackManager.addAction(backstackAction)
        }
    }

    /**
     * Deletes the whole selection. All [StoryStep] in the selection will be deleted.
     */
    fun deleteSelection() {
        coroutineScope.launch(dispatcher) {
            val (newStories, deletedStories) = writeopiaManager.bulkDelete(
                _positionsOnEdit.value,
                _currentStory.value.stories
            )

            backStackManager.addAction(BackstackAction.BulkDelete(deletedStories))
            _positionsOnEdit.value = emptySet()

            _currentStory.value =
                _currentStory.value.copy(stories = newStories, lastEdit = LastEdit.Whole)
        }
    }

    /**
     * Cancels the current selection.
     */
    private fun cancelSelection() {
        _positionsOnEdit.value = emptySet()
    }

    /**
     * Clears the [WriteopiaStateManager]. Use this in the onCleared of your ViewModel.
     */
    fun onClear() {
        coroutineScope.launch {
            sharedEditionManager?.stopLiveEdition()
        }.invokeOnCompletion {
            coroutineScope.cancel()
        }
    }

    companion object {
        fun create(
            writeopiaManager: WriteopiaManager,
            dispatcher: CoroutineDispatcher,
            stepsNormalizer: UnitsNormalizationMap =
                StepsMapNormalizationBuilder.reduceNormalizations {
                    defaultNormalizers()
                },
            movementHandler: MovementHandler = MovementHandler(),
            contentHandler: ContentHandler = ContentHandler(
                stepsNormalizer = stepsNormalizer
            ),
            coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
            backStackManager: BackstackManager = BackstackManager.create(
                contentHandler,
                movementHandler
            ),
            userId: suspend () -> String = { "no_user_id_provided" },
        ) = WriteopiaStateManager(
            stepsNormalizer,
            dispatcher,
            coroutineScope,
            backStackManager,
            userId,
            writeopiaManager
        )
    }
}

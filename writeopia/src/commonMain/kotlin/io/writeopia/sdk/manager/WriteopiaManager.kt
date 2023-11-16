package io.writeopia.sdk.manager

import io.writeopia.sdk.backstack.BackstackHandler
import io.writeopia.sdk.backstack.BackstackInform
import io.writeopia.sdk.backstack.BackstackManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.action.BackstackAction
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.model.document.DocumentInfo
import io.writeopia.sdk.model.document.info
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.model.story.DrawStory
import io.writeopia.sdk.model.story.LastEdit
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.StoryType
import io.writeopia.sdk.normalization.builder.StepsMapNormalizationBuilder
import io.writeopia.sdk.utils.alias.UnitsNormalizationMap
import io.writeopia.sdk.utils.extensions.toEditState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlin.coroutines.EmptyCoroutineContext

/**
 * This is the entry class of the framework. It follows the Controller pattern, redirecting all the
 * call to another class responsible for the part of the SDK requested.
 */
class WriteopiaManager(
    private val stepsNormalizer: UnitsNormalizationMap =
        StepsMapNormalizationBuilder.reduceNormalizations {
            defaultNormalizers()
        },
    private val movementHandler: MovementHandler = MovementHandler(stepsNormalizer),
    private val contentHandler: ContentHandler = ContentHandler(
        stepsNormalizer = stepsNormalizer
    ),
    private val focusHandler: FocusHandler = FocusHandler(),
    private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    private val backStackManager: BackstackManager = BackstackManager.create(
        contentHandler,
        movementHandler
    ),
    private val userId: suspend () -> String = { "no_user_id_provided" },
) : BackstackHandler, BackstackInform by backStackManager {

    private var localUserId: String? = null

    private val _scrollToPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val scrollToPosition: StateFlow<Int?> = _scrollToPosition.asStateFlow()

    private val _currentStory: MutableStateFlow<StoryState> = MutableStateFlow(
        StoryState(stories = initialContent(), lastEdit = LastEdit.Nothing)
    )

    private val _documentInfo: MutableStateFlow<DocumentInfo> =
        MutableStateFlow(DocumentInfo.empty())

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
            userId = localUserId ?: userId.invoke().also { id ->
                localUserId = id
            }
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

    private suspend fun getUserId(): String =
        localUserId ?: userId.invoke().also { id ->
            localUserId = id
        }

    private val isOnSelection: Boolean
        get() = _positionsOnEdit.value.isNotEmpty()


    /**
     * Saves the document automatically as it is changed. It uses the [DocumentTracker] passed
     * in the constructor of [WriteopiaManager]
     */
    fun saveOnStoryChanges(documentTracker: DocumentTracker) {
        coroutineScope.launch(dispatcher) {
            documentTracker.saveOnStoryChanges(_documentEditionState, getUserId())
        }
    }

    fun isInitialized(): Boolean = _currentStory.value.stories.isNotEmpty()

    /**
     * Creates a new story. Use this when you wouldn't like to load a documented previously saved.
     *
     * @param documentId the id of the document that will be created
     * @param title the title of the document
     */
    fun newStory(documentId: String = GenerateId.generate(), title: String = "") {
        val firstMessage = StoryStep(
            localId = GenerateId.generate(),
            type = StoryTypes.TITLE.type
        )
        val stories: Map<Int, StoryStep> = mapOf(0 to firstMessage)
        val normalized = stepsNormalizer(stories.toEditState())

        val now = Clock.System.now()

        _documentInfo.value = DocumentInfo(
            id = documentId,
            title = title,
            createdAt = now,
            lastUpdatedAt = now
        )

        _currentStory.value = StoryState(
            normalized + normalized,
            LastEdit.Nothing,
            firstMessage.id
        )
    }

    /**
     * Initializes a document passed as a parameter. This method should be used when you would like
     * to load a document from a database and start editing it, instead of creating something new.
     *
     * @param document [Document]
     */
    fun initDocument(document: Document) {
        if (isInitialized()) return

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
    //Todo: Add unit tests
    fun nextFocusOrCreate(position: Int) {
        coroutineScope.launch(dispatcher) {
            val storyMap = _currentStory.value.stories
            val nextFocus = focusHandler.findNextFocus(position, _currentStory.value.stories)
            if (nextFocus != null) {
                val (nextPosition, storyStep) = nextFocus
                val mutable = storyMap.toMutableMap()
                mutable[nextPosition] = storyStep.copy(localId = GenerateId.generate())
                _currentStory.value =
                    _currentStory.value.copy(stories = mutable, focusId = storyStep.id)
            }
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

        val movedStories = movementHandler.merge(_currentStory.value.stories, info)
        _currentStory.value = StoryState(
            stories = stepsNormalizer(movedStories),
            lastEdit = LastEdit.Whole
        )
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

        val newStory = movementHandler.move(_currentStory.value.stories, move)
        _currentStory.value = StoryState(newStory, lastEdit = LastEdit.Whole, move.storyStep.id)

        val backStackAction = BackstackAction.Move(
            storyStep = move.storyStep,
            positionFrom = move.positionFrom,
            positionTo = move.positionTo
        )
        backStackManager.addAction(backStackAction)
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     *
     * @param stateChange [Action.StoryStateChange]
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

    /**
     * Changes the story type. The type of a messages changes without changing the content of it.
     * Commands normally change the type of a message. From a message to a unordered list item, for
     * example.
     *
     * @param position Int
     * @param storyType [StoryStep]
     * @param commandInfo [CommandInfo]
     */
    fun changeStoryType(position: Int, storyType: StoryType, commandInfo: CommandInfo?) {
        if (isOnSelection) {
            cancelSelection()
        }

        _currentStory.value = contentHandler.changeStoryType(
            _currentStory.value.stories,
            storyType,
            position,
            commandInfo
        )
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
        val lastPosition = stories.size - 3
        val lastContentStory = stories[lastPosition]

        if (lastContentStory?.type == StoryTypes.TEXT.type) {
            val newStoriesState = stories.toMutableMap().apply {
                this[lastPosition] = lastContentStory.copyNewLocalId()
            }

            val newState =
                _currentStory.value.copy(focusId = lastContentStory.id, stories = newStoriesState)
            _currentStory.value = newState
        } else {
            var acc = stories.size - 1
            val newLastMessage =
                StoryStep(type = StoryTypes.TEXT.type)

            //Todo: It should be possible to customize which steps are add
            val newStories = stories + mapOf(
                acc++ to newLastMessage,
                acc++ to StoryStep(type = StoryTypes.SPACE.type),
                acc to StoryStep(type = StoryTypes.LAST_SPACE.type),
            )

            _currentStory.value = StoryState(newStories, LastEdit.Whole, newLastMessage.id)
        }
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

    /**
     * Deletes the whole selection. All [StoryStep] in the selection will be deleted.
     */
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

    /**
     * Cancels the current selection.
     */
    private fun cancelSelection() {
        _positionsOnEdit.value = emptySet()
    }

    /**
     * Clears the [WriteopiaManager]. Use this in the onCleared of your ViewModel.
     */
    fun onClear() {
        coroutineScope.cancel()
    }
}

fun initialContent() : Map<Int, StoryStep> = emptyMap()


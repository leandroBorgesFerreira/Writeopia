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
import io.writeopia.sdk.model.story.LastEdit
import io.writeopia.sdk.model.story.Selection
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.command.CommandInfo
import io.writeopia.sdk.models.command.CommandTrigger
import io.writeopia.sdk.models.command.TypeInfo
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import io.writeopia.sdk.models.story.TagInfo
import io.writeopia.sdk.normalization.builder.StepsMapNormalizationBuilder
import io.writeopia.sdk.sharededition.SharedEditionManager
import io.writeopia.sdk.utils.alias.UnitsNormalizationMap
import io.writeopia.sdk.utils.extensions.toEditState
import io.writeopia.ui.backstack.BackstackHandler
import io.writeopia.ui.backstack.BackstackInform
import io.writeopia.ui.backstack.BackstackManager
import io.writeopia.ui.edition.TextCommandHandler
import io.writeopia.ui.keyboard.KeyboardEvent
import io.writeopia.ui.model.DrawState
import io.writeopia.ui.model.DrawStory
import io.writeopia.ui.model.TextInput
import io.writeopia.ui.modifiers.StepsModifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.max
import kotlin.math.min

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
    private val writeopiaManager: WriteopiaManager,
    val selectionState: StateFlow<Boolean>,
    private val keyboardEventFlow: Flow<KeyboardEvent>,
    private val drawStateModify: (List<DrawStory>, Int) -> (List<DrawStory>) = StepsModifier::modify
) : BackstackHandler, BackstackInform by backStackManager {

    init {
        coroutineScope.launch(dispatcher) {
            keyboardEventFlow
                .onEach { delay(60) }
                .collect { event ->
                    when (event) {
                        KeyboardEvent.DELETE -> {
                            if (_positionsOnEdit.value.isNotEmpty()) {
                                deleteSelection()
                            }
                        }

                        KeyboardEvent.IDLE -> {}
                    }
                }
        }
    }

    private var lastLineBreak: LineBreakCommand? = null

    private val commandHandler = TextCommandHandler.defaultCommands(this)

    private var lastStateChange: Action.StoryStateChange? = null

    private val initialContent: Map<Int, StoryStep> =
        mapOf(0 to StoryStep(text = "", type = StoryTypes.TITLE.type))

    private var localUserId: String? = null

    private val _dragPosition = MutableStateFlow(-1)
    private val _isDragging = MutableStateFlow(false)

    private val dragRealPosition = combine(_dragPosition, _isDragging) { position, isDragging ->
        if (isDragging) position else -1
    }

    private val _scrollToPosition: MutableStateFlow<Int?> = MutableStateFlow(null)
    val scrollToPosition: StateFlow<Int?> = _scrollToPosition.asStateFlow()

    /**
     * The current story in focus
     */
    private val _currentStory: MutableStateFlow<StoryState> = MutableStateFlow(
        StoryState(stories = initialContent, lastEdit = LastEdit.Nothing)
    )

    private val _documentInfo: MutableStateFlow<DocumentInfo> =
        MutableStateFlow(DocumentInfo.empty())

    val documentInfo: StateFlow<DocumentInfo> = _documentInfo.asStateFlow()

    private val _positionsOnEdit = MutableStateFlow(setOf<Int>())
    val onEditPositions = _positionsOnEdit.asStateFlow()

    private var sharedEditionManager: SharedEditionManager? = null

    val currentStory: StateFlow<StoryState> = _currentStory.asStateFlow()

    val currentDocument: StateFlow<Document?> =
        combine(_documentInfo, _currentStory) { info, state ->
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
                parentId = info.parentId,
                isLocked = info.isLocked
            )
        }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val _documentEditionState: Flow<Pair<StoryState, DocumentInfo>> =
        combine(currentStory, _documentInfo, ::Pair)

    val toDraw: Flow<DrawState> =
        combine(
            _positionsOnEdit,
            currentStory,
            dragRealPosition
        ) { positions, storyState, dragPosition ->
            val focus = storyState.focus

            val toDrawStories = storyState.stories
                .mapValues { (position, storyStep) ->
                    DrawStory(
                        storyStep = storyStep,
                        cursor = storyState.selection.takeIf { it.position == position },
                        isSelected = positions.contains(position),
                        position = position
                    )
                }
                .values
                .toList()
                .let { drawStories -> drawStateModify(drawStories, dragPosition).drop(1) }

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
        parentFolder: String = "root",
        forceRestart: Boolean = false
    ) {
        if (isInitialized() && !forceRestart) return

        _initialized = true
        val (documentInfo, storyState) = writeopiaManager.newStory(
            documentId,
            title,
            parentFolder = parentFolder
        )

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
        val fixedMove = move.fixMove()

        if (_positionsOnEdit.value.contains(fixedMove.positionFrom)) {
            val bulkMove = Action.BulkMove(
                storyStep = selectedStories(),
                positionFrom = _positionsOnEdit.value,
                positionTo = fixedMove.positionTo
            )

            _currentStory.value = writeopiaManager.moveRequest(bulkMove, _currentStory.value)
        } else {
            _currentStory.value = writeopiaManager
                .moveRequest(fixedMove, _currentStory.value)
                .copy(focus = fixedMove.positionTo)

            val backStackAction = BackstackAction.Move(
                storyStep = fixedMove.storyStep,
                positionFrom = fixedMove.positionFrom,
                positionTo = fixedMove.positionTo
            )
            backStackManager.addAction(backStackAction)
        }

        cancelSelection()
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
     * Click lister when user clicks in the menu to add a check item
     */
    fun onCheckItemClicked() {
        val onEdit = _positionsOnEdit.value

        if (onEdit.isNotEmpty()) {
            toggleStateForStories(onEdit, StoryTypes.CHECK_ITEM)
        } else {
            changeCurrentStoryType(StoryTypes.CHECK_ITEM)
        }
    }

    /**
     * Click lister when user clicks in the menu to add a list item
     */
    fun onListItemClicked() {
        val onEdit = _positionsOnEdit.value

        if (onEdit.isNotEmpty()) {
            toggleStateForStories(onEdit, StoryTypes.UNORDERED_LIST_ITEM)
        } else {
            changeCurrentStoryType(StoryTypes.UNORDERED_LIST_ITEM)
        }
    }

    fun onHighLightBlockClicked() {
        val onEdit = _positionsOnEdit.value

        if (onEdit.isNotEmpty()) {
            toggleTagForStories(onEdit, TagInfo(Tag.HIGH_LIGHT_BLOCK))
        } else {
            currentFocus()?.first?.let { currentPosition ->
                toggleTagForStories(setOf(currentPosition), TagInfo(Tag.HIGH_LIGHT_BLOCK))
            }
        }
    }

    fun toggleTagForPosition(position: Int, tag: TagInfo, commandInfo: CommandInfo? = null) {
        val story = getStory(position)
        val storyText = story?.text

        val newState = if (commandInfo != null && storyText != null) {
            val commandText = commandInfo.command.commandText.trim()

            val newText = if (
                commandInfo.commandTrigger == CommandTrigger.WRITTEN &&
                story.text?.trim()?.startsWith(commandText.trim()) == true
            ) {
                storyText.subSequence(commandText.length, storyText.length).toString()
            } else {
                storyText
            }

            val mutable = currentStory.value.stories.toMutableMap()
            mutable[position] = story.copy(text = newText)
            mutable
        } else {
            currentStory.value.stories
        }

        toggleTagForStories(setOf(position), tag, newState)
    }

    /**
     * Click lister when user clicks in the menu to add a code block
     */
    fun onCodeBlockClicked() {
        //Todo: This change needs to take into account that code block is a multi line block and then
        //it needs to be have the relation N -> 1 and 1 -> N when transforming.
//        changeCurrentStoryType(StoryTypes.CODE_BLOCK)
    }

    fun toggleCollapseItem(position: Int) {
        val state = _currentStory.value
        val isCollapsed = currentStory.value
            .stories[position]
            ?.tags
            ?.any { it.tag == Tag.COLLAPSED } == true

        _currentStory.value = if (isCollapsed) {
            writeopiaManager.expandItem(state, position)
        } else {
            writeopiaManager.collapseItem(state, position)
        }
    }

    /**
     * At the moment it is only possible to check items not inside groups. Todo: Fix it!
     *
     * @param stateChange [Action.StoryStateChange]
     */
    private fun changeStoryText(stateChange: Action.StoryStateChange) {
        val backstackAction = _currentStory.value.stories[stateChange.position]?.let { oldStory ->
            BackstackAction.StoryTextChange(
                storyStep = oldStory,
                position = stateChange.position
            )
        }

        changeStoryStateAndTrackIt(stateChange, backstackAction)
    }

    /**
     * Changes the story type. The type of a messages changes without changing the content of it.
     * Commands normally change the type of a message. From a message to a unordered list item, for
     * example.
     *
     * @param position Int
     * @param typeInfo [TypeInfo]
     * @param commandInfo [CommandInfo]
     */
    fun changeStoryType(position: Int, typeInfo: TypeInfo, commandInfo: CommandInfo?) {
        if (isOnSelection) {
            cancelSelection()
        }

        _currentStory.value =
            writeopiaManager.changeStoryType(position, typeInfo, commandInfo, _currentStory.value)
    }

    fun removeTags(position: Int) {
        _currentStory.value =
            writeopiaManager.removeTags(position, _currentStory.value)
    }

    /**
     * Creates a line break. When a line break happens, the line it divided into two [StoryStep]s
     * of the same, if possible, or the next line will be a Message.
     *
     * @param lineBreak [Action.LineBreak]
     */
    fun onLineBreak(lineBreak: Action.LineBreak) {
        val lastBreak = lastLineBreak
        if (lastBreak != null
            && lastBreak.text == lineBreak.storyStep.text
            && lastBreak.position == lineBreak.position
            && (Clock.System.now()
                .toEpochMilliseconds() - lastBreak.time.toEpochMilliseconds() < 100)
        ) {
            return
        }

        lastLineBreak = LineBreakCommand(
            lineBreak.storyStep.text ?: "",
            lineBreak.position, Clock.System.now()
        )

        if (isOnSelection) {
            cancelSelection()
        }

        coroutineScope.launch(dispatcher) {
            val state = _currentStory.value
            val story = getStory(position = lineBreak.position)

            val expanded = if (story?.tags?.any { it.tag.isTitle() } == true) {
                writeopiaManager.expandItem(state, lineBreak.position)
            } else {
                state
            }

            writeopiaManager.onLineBreak(lineBreak, expanded)?.let { (info, newState) ->
                val (newPosition, newStory) = info
                // Todo: Fix this when the inner position are completed
                backStackManager.addAction(BackstackAction.Add(newStory, newPosition))
                _currentStory.value = newState.copy(selection = Selection.start())
                _scrollToPosition.value = info.first
            }
        }
    }

    fun onFocusChange(position: Int, hasFocus: Boolean) {
        if (!hasFocus) return
        val story = currentStory.value

        _currentStory.value = story.copy(focus = position)
    }

    /**
     * Add a [StoryStep] of a position into the selection list. Selected content can be used to
     * perform bulk actions, like bulk edition and bulk deletion.
     */
    fun onSelected(isSelected: Boolean, position: Int) {
        coroutineScope.launch(dispatcher) {
            if (_currentStory.value.stories[position] != null) {
                if (isSelected) {
                    _positionsOnEdit.value += position
                } else {
                    _positionsOnEdit.value -= position
                }
            }
        }
    }

    fun onSectionSelected(position: Int) {
        val isSelected = _positionsOnEdit.value.contains(position)
        val stories = getStories()

        val lastPosition = getStories().asSequence()
            .filter { (posi, _) -> posi > position }
            .find { (_, story) -> story.tags.any { it.tag.isTitle() } }
            ?.key
            ?: (stories.size - 1)

        val newSelected = buildSet {
            for (i in position .. lastPosition) {
                add(i)
            }
        }

        if (isSelected) {
            _positionsOnEdit.value -= newSelected
        } else {
            _positionsOnEdit.value += newSelected
        }
    }

    fun toggleSelection(position: Int) {
        onSelected(!_positionsOnEdit.value.contains(position), position)
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
            val cursor = lastContentStory.text?.length ?: 0

            _currentStory.value.copy(
                focus = lastPosition,
                stories = newStoriesState,
                selection = Selection.fromPosition(
                    cursorPosition = cursor,
                    stepPosition = lastPosition
                )
            )
        } else {
            val newLastMessage = StoryStep(type = StoryTypes.TEXT.type)
            val newStories = stories + mapOf(stories.size to newLastMessage)
            val cursor = newLastMessage.text?.length ?: 0

            StoryState(
                newStories,
                LastEdit.Whole,
                lastPosition,
                selection = Selection.fromPosition(
                    cursorPosition = cursor,
                    stepPosition = lastPosition
                )
            )
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

    fun onErase(eraseStory: Action.EraseStory) {
        coroutineScope.launch(dispatcher) {
            val previousInfo = writeopiaManager.previousTextStory(
                _currentStory.value.stories,
                eraseStory.position
            )
            val previousStory = previousInfo?.first
            val newFocus = previousInfo?.second

            _currentStory.value =
                writeopiaManager.onErase(eraseStory, _currentStory.value).let { state ->
                    if (previousStory != null && newFocus != null) {
                        state.copy(
                            selection = Selection.fromPosition(
                                previousStory.text?.length ?: 0,
                                newFocus
                            )
                        )
                    } else {
                        state
                    }
                }

            val backstackAction = BackstackAction.Erase(
                erasedStep = eraseStory.storyStep,
                receivingStep = previousStory,
                erasedPosition = eraseStory.position,
                receivingPosition = previousInfo?.second
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

    fun onDragHover(position: Int) {
        _dragPosition.value = position
    }

    fun onDragStart() {
        _isDragging.value = true
    }

    fun onDragStop() {
        coroutineScope.launch {
            // It is necessary to delay the stop dragging event to wait for the move request to
            // be received.
            delay(100)
            _isDragging.value = false
        }
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

    fun moveToNext(cursor: Int, positions: Int = 1) {
        val lastIndex = _currentStory.value.stories.size - 1

        val focusPosition = currentFocus()?.let { (position, _) -> position } ?: 0
        nextFocusOrCreate(min(focusPosition + positions, lastIndex), cursor)
    }

    fun moveToPrevious(cursor: Int, positions: Int = 1) {
        val focusPosition = currentFocus()?.let { (position, _) -> position } ?: 0
        nextFocusOrCreate(max(focusPosition - positions, 0), cursor)
    }

    fun toggleLockDocument() {
        val info = _documentInfo.value

        _documentInfo.value = info.copy(isLocked = !info.isLocked)
        _currentStory.value = currentStory.value.copy(lastEdit = LastEdit.Metadata)

    }

    /**
     * Moves the focus to the next available [StoryStep] if it can't find a step to focus, it
     * creates a new [StoryStep] at the end of the document.
     *
     * @param position Int
     */
    private fun nextFocusOrCreate(position: Int, cursor: Int) {
        coroutineScope.launch(dispatcher) {
            _currentStory.value =
                writeopiaManager.nextFocusOrCreate(position, cursor, _currentStory.value)
        }
    }

    private fun toggleStateForStories(onEdit: Set<Int>, storyTypes: StoryTypes) {
        val currentStories = currentStory.value.stories

        onEdit.forEach { position ->
            val story = currentStories[position]
            if (story != null) {
                val newType = if (story.type == storyTypes.type) {
                    StoryTypes.TEXT
                } else {
                    storyTypes
                }

                changeStoryState(
                    Action.StoryStateChange(
                        storyStep = story.copy(type = newType.type),
                        position = position,
                    )
                )
            }
        }
    }

    private fun toggleTagForStories(
        onEdit: Set<Int>,
        tag: TagInfo,
        currentStories: Map<Int, StoryStep> = currentStory.value.stories
    ) {

        onEdit.forEach { position ->
            val story = currentStories[position]
            if (story != null) {
                val currentTags = story.tags
                val newTags = if (currentTags.contains(tag)) {
                    currentTags.filterNot { it.tag == tag.tag }.toSet()
                } else {
                    currentTags + tag
                }

                changeStoryState(
                    Action.StoryStateChange(
                        storyStep = story.copy(tags = newTags),
                        position = position,
                    )
                )
            }
        }
    }

    private fun changeCurrentStoryType(storyTypes: StoryTypes) {
        changeCurrentStoryState { storyStep ->
            val newType = if (storyStep.type == storyTypes.type) {
                StoryTypes.TEXT.type
            } else {
                storyTypes.type
            }

            storyStep.copy(type = newType)
        }
    }

    private fun changeCurrentStoryState(stateChange: (StoryStep) -> StoryStep) {
        val currentStepEntry = currentFocus()

        if (currentStepEntry != null) {
            changeStoryState(
                Action.StoryStateChange(
                    stateChange(currentStepEntry.second),
                    currentStepEntry.first
                )
            )
        }
    }

    private fun currentFocus(): Pair<Int, StoryStep>? {
        val currentFocus = currentStory.value.focus

        return _currentStory.value
            .stories
            .entries
            .find { (position, _) -> position == currentFocus }
            ?.let { (position, step) ->
                position to step
            }
    }

    private fun changeStoryStateAndTrackIt(
        stateChange: Action.StoryStateChange,
        backstackAction: BackstackAction?
    ) {
        if (lastStateChange == stateChange) return
        lastStateChange = stateChange

        writeopiaManager.changeStoryState(stateChange, _currentStory.value)?.let { state ->
            _currentStory.value = state.copy(
                selection = Selection(
                    stateChange.selectionStart ?: 0,
                    stateChange.selectionEnd ?: 0,
                    stateChange.position
                )
            )
            backstackAction?.let(backStackManager::addAction)
        }
    }

    fun handleTextInput(
        input: TextInput,
        position: Int,
        lineBreakByContent: Boolean,
    ) {
        val text = input.text
        val step = _currentStory.value.stories[position] ?: return

        if (lineBreakByContent && text.contains("\n")) {
            val newStep = step.copy(text = text)
            onLineBreak(Action.LineBreak(newStep, position))
        } else {
            val newText = text.replace("\n", "")
            val newStep = step.copy(text = newText)
            val handled = commandHandler.handleCommand(text, newStep, position)

            if (!handled) {
                changeStoryText(
                    Action.StoryStateChange(
                        newStep,
                        position,
                        selectionStart = input.start,
                        selectionEnd = input.end,
                    )
                )
            }
        }
    }

    private fun selectedStories(): List<StoryStep> = _positionsOnEdit.value.mapNotNull(::getStory)

    /**
     * Cancels the current selection.
     */
    public fun cancelSelection() {
        _positionsOnEdit.value = emptySet()
    }

    private fun getStory(position: Int): StoryStep? = _currentStory.value.stories[position]

    private fun getStories() = _currentStory.value.stories

    companion object {
        fun create(
            writeopiaManager: WriteopiaManager,
            dispatcher: CoroutineDispatcher,
            selectionState: StateFlow<Boolean> = MutableStateFlow(false),
            keyboardEventFlow: Flow<KeyboardEvent?> = MutableStateFlow(null),
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
            writeopiaManager,
            selectionState,
            keyboardEventFlow.filterNotNull(),
            StepsModifier::modify
        )
    }
}

private class LineBreakCommand(val text: String, val position: Int, val time: Instant)

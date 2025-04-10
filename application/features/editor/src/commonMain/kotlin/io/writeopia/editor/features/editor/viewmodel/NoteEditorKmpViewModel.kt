package io.writeopia.editor.features.editor.viewmodel

import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.writeopia.OllamaRepository
import io.writeopia.auth.core.utils.USER_OFFLINE
import io.writeopia.common.utils.ResultData
import io.writeopia.common.utils.collections.toNodeTree
import io.writeopia.common.utils.file.SaveImage
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.common.utils.toList
import io.writeopia.commonui.dtos.MenuItemUi
import io.writeopia.commonui.extensions.toFolderUi
import io.writeopia.core.folders.repository.FolderRepository
import io.writeopia.editor.features.editor.copy.CopyManager
import io.writeopia.editor.model.EditState
import io.writeopia.model.Font
import io.writeopia.models.configuration.WorkspaceConfigRepository
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sdk.export.DocumentToJson
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.export.DocumentWriter
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.files.ExternalFile
import io.writeopia.sdk.models.span.Span
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.persistence.core.tracker.OnUpdateDocumentTracker
import io.writeopia.sdk.repository.DocumentRepository
import io.writeopia.sdk.serialization.extensions.toApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sdk.serialization.request.wrapInRequest
import io.writeopia.sdk.sharededition.SharedEditionManager
import io.writeopia.sdk.utils.extensions.noContent
import io.writeopia.ui.backstack.BackstackHandler
import io.writeopia.ui.backstack.BackstackInform
import io.writeopia.ui.keyboard.KeyboardEvent
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawState
import io.writeopia.ui.utils.Spans
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class NoteEditorKmpViewModel(
    override val writeopiaManager: WriteopiaStateManager,
    private val documentRepository: DocumentRepository,
    private val sharedEditionManager: SharedEditionManager,
    private val parentFolderId: String,
    private val uiConfigurationRepository: UiConfigurationRepository,
    private val documentToMarkdown: DocumentToMarkdown = DocumentToMarkdown,
    private val documentToJson: DocumentToJson = DocumentToJson(),
    private val folderRepository: FolderRepository,
    private val ollamaRepository: OllamaRepository? = null,
    private val workspaceConfigRepository: WorkspaceConfigRepository,
    private val keyboardEventFlow: Flow<KeyboardEvent>,
    private val copyManager: CopyManager,
) : NoteEditorViewModel,
    ViewModel(),
    BackstackInform by writeopiaManager,
    BackstackHandler by writeopiaManager {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            keyboardEventFlow
                .onEach { delay(60) }
                .collect { event ->
                    when (event) {
                        KeyboardEvent.LOCAL_SAVE -> {
                            saveDocumentInWorkSpace()
                        }

                        KeyboardEvent.COPY -> {
                            copySelection()
                        }

                        KeyboardEvent.AI_QUESTION -> {
                            askAiBySelection()
                        }

                        KeyboardEvent.CANCEL -> {
                            writeopiaManager.clearSelection()
                        }

                        KeyboardEvent.UNDO -> {
                            writeopiaManager.undo()
                        }

                        KeyboardEvent.REDO -> {
                            writeopiaManager.redo()
                        }

                        else -> {}
                    }
                }
        }
    }

    /**
     * This property defines if the document should be edited (you can write in it, for example)
     */
    override val isEditable: StateFlow<Boolean> = writeopiaManager
        .documentInfo
        .map { info -> !info.isLocked }
        .stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = false)

    private val _showGlobalMenu = MutableStateFlow(false)
    override val showGlobalMenu = _showGlobalMenu.asStateFlow()

    private val _editHeader = MutableStateFlow(false)
    override val editHeader = _editHeader.asStateFlow()

    override val currentTitle by lazy {
        writeopiaManager.currentDocument.filterNotNull().map { document ->
            document.title
        }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = "")
    }

    private val _shouldGoToNextScreen = MutableStateFlow(false)
    override val shouldGoToNextScreen = _shouldGoToNextScreen.asStateFlow()

    private val _expandedFolders = MutableStateFlow(setOf<String>())

    override val isEditState: StateFlow<EditState> by lazy {
        writeopiaManager.onEditPositions.map { set ->
            when {
                set.isNotEmpty() -> EditState.SELECTED_TEXT

                else -> EditState.TEXT
            }
        }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = EditState.TEXT)
    }

    private val story: StateFlow<StoryState> = writeopiaManager.currentStory
    override val scrollToPosition = writeopiaManager.scrollToPosition

    private val _loadingState = MutableStateFlow(false)
    override val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private val documentId: StateFlow<String> by lazy {
        writeopiaManager.documentInfo
            .map { it.id }
            .filterNotNull()
            .stateIn(viewModelScope, SharingStarted.Lazily, "")
    }

    /**
     * This property defines if the document is favorite
     */
    override val notFavorite: StateFlow<Boolean> = writeopiaManager
        .documentInfo
        .map { info -> !info.isFavorite }
        .stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val toDrawWithDecoration: StateFlow<DrawState> by lazy {
        val infoFlow = documentId.flatMapLatest {
            documentRepository.listenForDocumentInfoById(it)
        }

        writeopiaManager.toDraw.flatMapLatest { drawState ->
            infoFlow.map { info ->
                drawState to info
            }
        }.map { (drawState, info) ->
            val imageVector = info?.icon
                ?.label
                ?.let(WrIcons::fromName)

            val tint = info?.icon?.tint

            val newStories = drawState.stories
                .map { drawStory ->
                    if (drawStory.storyStep.type == StoryTypes.TITLE.type) {
                        val extraInfo = mutableMapOf<String, Any>()

                        if (imageVector != null) {
                            extraInfo["imageVector"] = imageVector
                        }

                        if (tint != null) {
                            extraInfo["imageVectorTint"] = tint
                        }

                        drawStory.copy(extraInfo = extraInfo)
                    } else {
                        drawStory
                    }
                }

            drawState.copy(stories = newStories)
        }.stateIn(viewModelScope, SharingStarted.Lazily, DrawState(emptyList()))
    }

    private val _documentToShareInfo = MutableStateFlow<ShareDocument?>(null)
    override val documentToShareInfo: StateFlow<ShareDocument?> = _documentToShareInfo.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val listenForFolders: StateFlow<List<MenuItemUi.FolderUi>> =
        MutableStateFlow("root")
            .flatMapLatest {
                combine(
                    _expandedFolders,
                    folderRepository.listenForFoldersByParentId("root")
                ) { expanded, map ->
                    val folderUiMap = map.mapValues { (_, item) ->
                        item.map {
                            it.toFolderUi(expanded = expanded.contains(it.id))
                        }
                    }

                    folderUiMap
                        .toNodeTree(
                            MenuItemUi.FolderUi.root(),
                            filterPredicate = { menuItemUi ->
                                menuItemUi.expanded
                            }
                        )
                        .toList()
                        .filter { it.id != "root" }
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override fun deleteSelection() {
        writeopiaManager.deleteSelection()
    }

    override fun handleBackAction(navigateBack: () -> Unit) {
        when {
            showGlobalMenu.value -> {
                _showGlobalMenu.value = false
            }

            editHeader.value -> {
                _editHeader.value = false
            }

            else -> {
                removeNoteIfEmpty(navigateBack)
            }
        }
    }

    override fun onHeaderClick() {
        if (!isEditable.value) return
        _editHeader.value = true
    }

    override fun createNewDocument(documentId: String, title: String) {
        if (writeopiaManager.isInitialized()) {
            return
        }

        writeopiaManager.newDocument(documentId, title, parentFolder = parentFolderId)
        writeopiaManager.saveOnStoryChanges(OnUpdateDocumentTracker(documentRepository))
        writeopiaManager.liveSync(sharedEditionManager)
    }

    override fun loadDocument(documentId: String) {
        if (writeopiaManager.isInitialized()) return

        viewModelScope.launch(Dispatchers.Default) {
            val document = documentRepository.loadDocumentById(documentId)

            if (document != null) {
                writeopiaManager.loadDocument(document)
                writeopiaManager.saveOnStoryChanges(
                    OnUpdateDocumentTracker(
                        documentRepository
                    )
                )
            }
        }
    }

    override fun onHeaderColorSelection(color: Int?) {
        if (!isEditable.value) return

        onHeaderEditionCancel()

        writeopiaManager.currentStory.value.stories[0]?.let { storyStep ->
            val action = Action.StoryStateChange(
                storyStep = storyStep.copy(
                    decoration = storyStep.decoration.copy(
                        backgroundColor = color
                    )
                ),
                position = 0
            )
            writeopiaManager.changeStoryState(action)
        }
    }

    override fun onAddCheckListClick() {
        if (!isEditable.value) return
        writeopiaManager.onCheckItemClicked()
    }

    override fun onAddListItemClick() {
        if (!isEditable.value) return
        writeopiaManager.onListItemClicked()
    }

    override fun onAddCodeBlockClick() {
        if (!isEditable.value) return
        writeopiaManager.onCodeBlockClicked()
    }

    override fun toggleHighLightBlock() {
        if (!isEditable.value) return
        writeopiaManager.toggleHighLightBlock()
    }

    override fun onHeaderEditionCancel() {
        _editHeader.value = false
    }

    override fun onMoreOptionsClick() {
        _showGlobalMenu.value = !_showGlobalMenu.value
    }

    override fun shareDocumentInJson() {
        shareDocument(::documentToJson, "application/json")
    }

    override fun shareDocumentInMarkdown() {
        shareDocument(::documentToMd, "plain/text")
    }

    override fun onViewModelCleared() {
        writeopiaManager.onClear()
    }

    override fun clearSelections() {
        writeopiaManager.clearSelection()
    }

    override val fontFamily: StateFlow<Font> by lazy {
        uiConfigurationRepository.listenForUiConfiguration({ USER_OFFLINE }, viewModelScope)
            .filterNotNull()
            .map { it.font }
            .stateIn(viewModelScope, SharingStarted.Lazily, Font.SYSTEM)
    }

    override fun onAddSpanClick(span: Span) {
        viewModelScope.launch(Dispatchers.Default) {
            writeopiaManager.toggleSpan(span)
        }
    }

    override fun toggleEditable() {
        writeopiaManager.toggleLockDocument()
    }

    override fun toggleFavorite() {
        writeopiaManager.toggleFavoriteDocument()
    }

    override fun changeFontFamily(font: Font) {
        viewModelScope.launch {
            uiConfigurationRepository.updateConfiguration(USER_OFFLINE) { config ->
                config.copy(font = font)
            }
        }
    }

    override fun addImage(imagePath: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val path = workspaceConfigRepository
                .loadWorkspacePath("disconnected_user")
                ?.let { workspace ->
                    SaveImage.saveLocally(
                        imagePath,
                        "$workspace/images"
                    )
                } ?: imagePath

            writeopiaManager.addImage(path)
        }
    }

    override fun exportMarkdown(path: String) {
        viewModelScope.launch(Dispatchers.Default) {
            writeDocument(path, documentToMarkdown)
        }
    }

    override fun exportJson(path: String) {
        viewModelScope.launch(Dispatchers.Default) {
            writeDocument(path, documentToJson)
        }
    }

    override fun expandFolder(folderId: String) {
        val expanded = _expandedFolders.value
        if (expanded.contains(folderId)) {
            viewModelScope.launch(Dispatchers.Default) {
                _expandedFolders.value = expanded - folderId
            }
        } else {
            viewModelScope.launch {
                folderRepository.listenForFoldersByParentId(folderId)
                _expandedFolders.value = expanded + folderId
            }
        }
    }

    override fun moveToFolder(folderId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            documentRepository.moveToFolder(documentId = documentId.value, parentId = folderId)
        }
    }

    override fun moveToRootFolder() {
        viewModelScope.launch(Dispatchers.Default) {
            documentRepository.moveToFolder(documentId = documentId.value, parentId = "root")
        }
    }

    override fun askAiBySelection() {
        if (ollamaRepository == null) return

        viewModelScope.launch(Dispatchers.Default) {
            val text = writeopiaManager.getCurrentText()
            val position = writeopiaManager.getNextPosition()

            if (text != null && position != null) {
                val url = ollamaRepository.getConfiguredOllamaUrl()?.trim()

                if (url == null) {
                    writeopiaManager.changeStoryState(
                        Action.StoryStateChange(
                            storyStep = StoryStep(
                                type = StoryTypes.AI_ANSWER.type,
                                text = "Ollama is not configured or not running."
                            ),
                            position = position,
                        )
                    )
                } else {
                    val model = ollamaRepository.getOllamaSelectedModel("disconnected_user")
                        ?: return@launch

                    ollamaRepository.streamReply(model, text, url)
                        .onStart {
                            writeopiaManager.addAtPosition(
                                storyStep = StoryStep(
                                    type = StoryTypes.LOADING.type,
                                    ephemeral = true
                                ),
                                position = position
                            )
                        }
                        .onCompletion {
                            writeopiaManager.trackState()
                        }
                        .collect { result ->
                            val text = when (result) {
                                is ResultData.Complete -> result.data
                                is ResultData.Error -> "Error. Message: ${result.exception?.message}"
                                is ResultData.Loading,
                                is ResultData.Idle,
                                is ResultData.InProgress -> ""
                            }

                            writeopiaManager.changeStoryState(
                                Action.StoryStateChange(
                                    storyStep = StoryStep(
                                        type = StoryTypes.AI_ANSWER.type,
                                        text = text
                                    ),
                                    position = position,
                                ),
                                trackIt = false
                            )
                        }
                }
            }
        }
    }

    override fun aiSummary() {
        if (ollamaRepository == null) return

        documentPrompt(ollamaRepository::streamSummary)
    }

    override fun aiActionPoints() {
        if (ollamaRepository == null) return

        documentPrompt(ollamaRepository::streamActionsPoints)
    }

    override fun aiFaq() {
        if (ollamaRepository == null) return

        documentPrompt(ollamaRepository::streamFaq)
    }

    override fun aiTags() {
        if (ollamaRepository == null) return

        documentPrompt(ollamaRepository::streamTags)
    }

    override fun addPage() {
        viewModelScope.launch(Dispatchers.Default) {
            writeopiaManager.addLinkToDocument()
        }
    }

    override fun copySelection() {
        if (!writeopiaManager.isOnSelection) return

        val lineBreak = buildAnnotatedString { append("\n") }

        val annotatedString = writeopiaManager.getSelectedStories()
            .filter { storyStep -> storyStep.text != null }
            .map { storyStep ->
                val text = storyStep.text ?: ""

                Spans.createStringWithSpans(text, storyStep.spans)
            }.reduce { acc, annotatedString ->
                acc + lineBreak + annotatedString
            }

        copyManager.copy(annotatedString)
    }

    override fun deleteDocument() {
        viewModelScope.launch(Dispatchers.Default) {
            documentRepository.deleteDocument(writeopiaManager.getDocument())
        }
    }

    override fun receiveExternalFile(files: List<ExternalFile>, position: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val newFiles = workspaceConfigRepository
                .loadWorkspacePath("disconnected_user")
                ?.let { workspace ->
                    files.map { file ->
                        if (writeopiaManager.supportedImageFiles.contains(file.extension)) {
                            val newPath = SaveImage.saveLocally(file.fullPath, "$workspace/images")

                            file.copy(fullPath = newPath)
                        } else {
                            file
                        }
                    }
                } ?: files

            writeopiaManager.receiveExternalFiles(newFiles, position)
        }
    }

    private fun documentPrompt(promptFn: (String, String, String) -> Flow<ResultData<String>>) {
        if (ollamaRepository == null) return

        viewModelScope.launch(Dispatchers.Default) {
            val text = writeopiaManager.getCurrentSelectionText()
                ?: writeopiaManager.getDocumentText()

            val position =
                writeopiaManager.positionAfterSelection() ?: writeopiaManager.lastPosition()

            val url = ollamaRepository.getConfiguredOllamaUrl()?.trim()

            if (url == null) {
                writeopiaManager.changeStoryState(
                    Action.StoryStateChange(
                        storyStep = StoryStep(
                            type = StoryTypes.AI_ANSWER.type,
                            text = "Ollama is not configured or not running."
                        ),
                        position = position,
                    )
                )
            } else {
                val model = ollamaRepository.getOllamaSelectedModel("disconnected_user")
                    ?: return@launch

                promptFn(model, text, url)
                    .onStart {
                        writeopiaManager.addAtPosition(
                            storyStep = StoryStep(
                                type = StoryTypes.LOADING.type,
                                ephemeral = true
                            ),
                            position = position
                        )
                    }
                    .onCompletion {
                        writeopiaManager.trackState()
                    }
                    .collect { result ->
                        val text = when (result) {
                            is ResultData.Complete -> result.data
                            is ResultData.Error -> "Error. Message: ${result.exception?.message}"
                            is ResultData.Loading,
                            is ResultData.Idle,
                            is ResultData.InProgress -> ""
                        }

                        writeopiaManager.changeStoryState(
                            Action.StoryStateChange(
                                storyStep = StoryStep(
                                    type = StoryTypes.AI_ANSWER.type,
                                    text = text
                                ),
                                position = position,
                            ),
                            trackIt = false
                        )
                    }
            }
        }
    }

    private fun saveDocumentInWorkSpace() {
        viewModelScope.launch(Dispatchers.Default) {
            _loadingState.value = true

            val document = writeopiaManager.getDocument()
            val path = workspaceConfigRepository.loadWorkspacePath("disconnected_user")

            // Todo: When path is null, the user should be asked to configure it.
            if (path != null) {
                documentToJson.writeDocument(
                    document = document,
                    path = path,
                    writeConfigFile = true,
                )
            }

            // Some delay so users can see a loading state
            delay(150)
            _loadingState.value = false
        }
    }

    private fun writeDocument(path: String, writer: DocumentWriter) {
        writer.writeDocuments(
            documents = listOf(writeopiaManager.getDocument()),
            path = path,
            writeConfigFile = false,
            usePath = false
        )
    }

    private fun documentToJson(document: Document, json: Json = writeopiaJson): String {
        val request = document.toApi().wrapInRequest()
        return json.encodeToString(request)
    }

    private fun documentToMd(document: Document): String =
        DocumentToMarkdown.parse(document.content)

    private fun shareDocument(infoParse: (Document) -> String, type: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val document: Document = writeopiaManager.currentDocument
                .stateIn(this)
                .value ?: return@launch

            val documentTitle = document.title.replace(" ", "_")

            val newContent = document.copy(content = document.content)
            val stringDocument = infoParse(newContent)

            _documentToShareInfo.emit(ShareDocument(stringDocument, documentTitle, type))
        }
    }

    private fun removeNoteIfEmpty(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            val document = writeopiaManager.currentDocument.stateIn(this).value

            if (document != null && story.value.stories.noContent()) {
                documentRepository.deleteDocument(document)
            }

            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }
}

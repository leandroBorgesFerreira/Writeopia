package io.writeopia.editor.features.editor.viewmodel

import io.writeopia.auth.core.utils.USER_OFFLINE
import io.writeopia.common.utils.KmpViewModel
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.editor.model.EditState
import io.writeopia.model.Font
import io.writeopia.repository.UiConfigurationRepository
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.filter.DocumentFilter
import io.writeopia.sdk.filter.DocumentFilterObject
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.tracker.OnUpdateDocumentTracker
import io.writeopia.sdk.serialization.extensions.toApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sdk.serialization.request.wrapInRequest
import io.writeopia.sdk.sharededition.SharedEditionManager
import io.writeopia.sdk.utils.extensions.noContent
import io.writeopia.ui.backstack.BackstackHandler
import io.writeopia.ui.backstack.BackstackInform
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.DrawState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NoteEditorKmpViewModel(
    override val writeopiaManager: WriteopiaStateManager,
    private val documentRepository: DocumentRepository,
    private val documentFilter: DocumentFilter = DocumentFilterObject,
    private val sharedEditionManager: SharedEditionManager,
    private val parentFolderId: String,
    private val uiConfigurationRepository: UiConfigurationRepository
) : NoteEditorViewModel,
    KmpViewModel(),
    BackstackInform by writeopiaManager,
    BackstackHandler by writeopiaManager {

    private val isEditableState = MutableStateFlow(true)

    /**
     * This property defines if the document should be edited (you can write in it, for example)
     */
    override val isEditable: StateFlow<Boolean> = isEditableState

    private val _showGlobalMenu = MutableStateFlow(false)
    override val showGlobalMenu = _showGlobalMenu.asStateFlow()

    private val _editHeader = MutableStateFlow(false)
    override val editHeader = _editHeader.asStateFlow()

    override val currentTitle by lazy {
        writeopiaManager.currentDocument.filterNotNull().map { document ->
            document.title
        }.stateIn(coroutineScope, started = SharingStarted.Lazily, initialValue = "")
    }

    private val _shouldGoToNextScreen = MutableStateFlow(false)
    override val shouldGoToNextScreen = _shouldGoToNextScreen.asStateFlow()

    override val isEditState: StateFlow<EditState> by lazy {
        writeopiaManager.onEditPositions.map { set ->
            when {
                set.isNotEmpty() -> EditState.SELECTED_TEXT

                else -> EditState.TEXT
            }
        }.stateIn(coroutineScope, started = SharingStarted.Lazily, initialValue = EditState.TEXT)
    }

    private val story: StateFlow<StoryState> = writeopiaManager.currentStory
    override val scrollToPosition = writeopiaManager.scrollToPosition

    private val documentId: StateFlow<String> by lazy {
        writeopiaManager.documentInfo
            .map { it.id }
            .filterNotNull()
            .stateIn(coroutineScope, SharingStarted.Lazily, "")
    }

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
        }.stateIn(coroutineScope, SharingStarted.Lazily, DrawState(emptyList()))
    }

    private val _documentToShareInfo = MutableStateFlow<ShareDocument?>(null)
    override val documentToShareInfo: StateFlow<ShareDocument?> = _documentToShareInfo.asStateFlow()

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
        _editHeader.value = true
    }

    override fun createNewDocument(documentId: String, title: String) {
        if (writeopiaManager.isInitialized()) {
            return
        }

        writeopiaManager.newStory(documentId, title, parentFolder = parentFolderId)
        writeopiaManager.saveOnStoryChanges(OnUpdateDocumentTracker(documentRepository))
        writeopiaManager.liveSync(sharedEditionManager)
    }

    override fun loadDocument(documentId: String) {
        if (writeopiaManager.isInitialized()) return

        coroutineScope.launch(Dispatchers.Default) {
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
        writeopiaManager.onCheckItemClicked()
    }

    override fun onAddListItemClick() {
        writeopiaManager.onListItemClicked()
    }

    override fun onAddCodeBlockClick() {
        writeopiaManager.onCodeBlockClicked()
    }

    override fun onAddHighLightBlockClick() {
        writeopiaManager.onHighLightBlockClicked()
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
        writeopiaManager.cancelSelection()
    }

    override val fontFamily: StateFlow<Font> by lazy {
        uiConfigurationRepository.listenForUiConfiguration({ USER_OFFLINE }, coroutineScope)
            .filterNotNull()
            .map { it.font }
            .stateIn(coroutineScope, SharingStarted.Lazily, Font.SYSTEM)
    }

    override fun changeFontFamily(font: Font) {
        coroutineScope.launch {
            uiConfigurationRepository.updateConfiguration(USER_OFFLINE) { config ->
                config.copy(font = font)
            }
        }
    }

    private fun documentToJson(document: Document, json: Json = writeopiaJson): String {
        val request = document.toApi().wrapInRequest()
        return json.encodeToString(request)
    }

    private fun documentToMd(document: Document): String =
        DocumentToMarkdown.parse(document.content)

    private fun shareDocument(infoParse: (Document) -> String, type: String) {
        coroutineScope.launch {
            val document: Document = writeopiaManager.currentDocument
                .stateIn(coroutineScope)
                .value ?: return@launch

            val documentTitle = document.title.replace(" ", "_")
            val filteredContent = documentFilter.removeTypesFromDocument(document.content)

            val newContent = document.copy(content = filteredContent)
            val stringDocument = infoParse(newContent)

            _documentToShareInfo.emit(ShareDocument(stringDocument, documentTitle, type))
        }
    }

    private fun removeNoteIfEmpty(onComplete: () -> Unit) {
        coroutineScope.launch(Dispatchers.Default) {
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

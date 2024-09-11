package io.writeopia.editor.viewmodel

import io.writeopia.editor.model.EditState
import io.writeopia.ui.backstack.BackstackHandler
import io.writeopia.ui.backstack.BackstackInform
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.filter.DocumentFilter
import io.writeopia.sdk.filter.DocumentFilterObject
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.tracker.OnUpdateDocumentTracker
import io.writeopia.sdk.serialization.extensions.toApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sdk.serialization.request.wrapInRequest
import io.writeopia.sdk.sharededition.SharedEditionManager
import io.writeopia.sdk.utils.extensions.noContent
import io.writeopia.utils_module.KmpViewModel
import kotlinx.coroutines.Dispatchers
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
    private val parentFolderId: String
) : NoteEditorViewModel, KmpViewModel(), BackstackInform by writeopiaManager,
    BackstackHandler by writeopiaManager {

    private val _isEditableState = MutableStateFlow(true)

    /**
     * This property defines if the document should be edited (you can write in it, for example)
     */
    override val isEditable: StateFlow<Boolean> = _isEditableState

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

    override val toDraw: StateFlow<DrawState> by lazy {
        writeopiaManager.toDraw.stateIn(
            coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = DrawState(emptyMap())
        )
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
        //Todo: There is a problem here!!
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


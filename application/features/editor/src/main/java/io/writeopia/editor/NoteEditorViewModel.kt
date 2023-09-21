package io.writeopia.editor

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.writeopia.editor.model.EditState
import io.writeopia.sdk.backstack.BackstackHandler
import io.writeopia.sdk.backstack.BackstackInform
import io.writeopia.sdk.export.DocumentToMarkdown
import io.writeopia.sdk.filter.DocumentFilter
import io.writeopia.sdk.filter.DocumentFilterObject
import io.writeopia.sdk.manager.DocumentRepository
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.story.DrawState
import io.writeopia.sdk.model.story.StoryState
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.Decoration
import io.writeopia.sdk.persistence.tracker.OnUpdateDocumentTracker
import io.writeopia.sdk.serialization.extensions.toApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import io.writeopia.sdk.serialization.request.wrapInRequest
import io.writeopia.sdk.utils.extensions.noContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class NoteEditorViewModel(
    val writeopiaManager: WriteopiaManager,
    private val documentRepository: DocumentRepository,
    private val documentFilter: DocumentFilter = DocumentFilterObject,
) : ViewModel(),
    BackstackInform by writeopiaManager,
    BackstackHandler by writeopiaManager {

    private val _isEditableState = MutableStateFlow(true)

    /**
     * This property defines if the document should be edited (you can write in it, for example)
     */
    val isEditable: StateFlow<Boolean> = _isEditableState

    private val _showGlobalMenu = MutableStateFlow(false)
    val showGlobalMenu = _showGlobalMenu.asStateFlow()

    private val _editHeader = MutableStateFlow(false)
    val editHeader = _editHeader.asStateFlow()

    val currentTitle = writeopiaManager.currentDocument.filterNotNull().map { document ->
        document.title
    }

    private val _shouldGoToNextScreen = MutableStateFlow(false)
    val shouldGoToNextScreen = _shouldGoToNextScreen.asStateFlow()

    val isEditState: StateFlow<EditState> = writeopiaManager.onEditPositions.map { set ->
        when {
            set.isNotEmpty() -> EditState.SELECTED_TEXT

            else -> EditState.TEXT
        }
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = EditState.TEXT)

    private val story: StateFlow<StoryState> = writeopiaManager.currentStory
    val scrollToPosition = writeopiaManager.scrollToPosition
    val toDraw: StateFlow<DrawState> = writeopiaManager.toDraw.stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DrawState(emptyMap())
    )

    fun deleteSelection() {
        writeopiaManager.deleteSelection()
    }

    fun handleBackAction(navigateBack: () -> Unit) {
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

    fun onHeaderClick() {
        _editHeader.value = true
    }

    fun createNewDocument(documentId: String, title: String) {
        if (writeopiaManager.isInitialized()) return

        writeopiaManager.newStory(documentId, title)

        viewModelScope.launch {
            writeopiaManager.currentDocument.stateIn(this).value?.let { document ->
                documentRepository.saveDocument(document)
                writeopiaManager.saveOnStoryChanges(OnUpdateDocumentTracker(documentRepository))
            }
        }
    }

    fun requestDocumentContent(documentId: String) {
        if (writeopiaManager.isInitialized()) return

        viewModelScope.launch(Dispatchers.IO) {
            val document = documentRepository.loadDocumentById(documentId)

            if (document != null) {
                writeopiaManager.initDocument(document)
                writeopiaManager.saveOnStoryChanges(OnUpdateDocumentTracker(documentRepository))
            }
        }
    }

    private fun removeNoteIfEmpty(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val document = writeopiaManager.currentDocument.stateIn(this).value

            if (document != null && story.value.stories.noContent()) {
                documentRepository.deleteDocument(document)
            }

            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    fun onHeaderColorSelection(color: Int?) {
        onHeaderEditionCancel()
        writeopiaManager.currentStory.value.stories[0]?.let { storyStep ->
            val action = Action.StoryStateChange(
                storyStep = storyStep.copy(
                    decoration = Decoration(
                        backgroundColor = color
                    )
                ),
                position = 0
            )
            writeopiaManager.changeStoryState(action)
        }
    }

    fun onHeaderEditionCancel() {
        _editHeader.value = false
    }

    fun onMoreOptionsClick() {
        _showGlobalMenu.value = !_showGlobalMenu.value
    }

    fun shareDocumentInJson(context: Context) {
        shareDocument(context, ::documentToJson, "application/json")
    }

    fun shareDocumentInMarkdown(context: Context) {
        shareDocument(context, ::documentToMd, "plain/text")
    }

    private fun documentToJson(document: Document, json: Json = writeopiaJson): String {
        val request = document.toApi().wrapInRequest()
        return json.encodeToString(request)
    }

    private fun documentToMd(document: Document): String =
        DocumentToMarkdown.parse(document.content)

    private fun shareDocument(context: Context, infoParse: (Document) -> String, type: String) {
        viewModelScope.launch {
            val document: Document = writeopiaManager.currentDocument
                .stateIn(viewModelScope)
                .value ?: return@launch

            val documentTitle = document.title.replace(" ", "_")
            val filteredContent = documentFilter.removeTypesFromDocument(document.content)

            val newContent = document.copy(content = filteredContent)
            val jsonDocument = infoParse(newContent)

            val intent = Intent(Intent.ACTION_SEND).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(Intent.EXTRA_TEXT, jsonDocument)
                putExtra(Intent.EXTRA_TITLE, documentTitle)
                action = Intent.ACTION_SEND
                this.type = type
            }

            context.startActivity(
                Intent.createChooser(intent, "Export Document")
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        writeopiaManager.onClear()
    }
}


package io.storiesteller.editor

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.storiesteller.sdk.backstack.BackstackHandler
import io.storiesteller.sdk.backstack.BackstackInform
import io.storiesteller.sdk.filter.DocumentFilter
import io.storiesteller.sdk.filter.DocumentFilterObject
import io.storiesteller.sdk.manager.DocumentRepository
import io.storiesteller.sdk.manager.StoriesTellerManager
import io.storiesteller.sdk.model.action.Action
import io.storiesteller.sdk.model.story.DrawState
import io.storiesteller.sdk.model.story.StoryState
import io.storiesteller.sdk.models.document.Document
import io.storiesteller.sdk.models.story.Decoration
import io.storiesteller.sdk.serialization.extensions.toApi
import io.storiesteller.sdk.serialization.json.storiesTellerJson
import io.storiesteller.sdk.serialization.request.wrapInRequest
import io.storiesteller.sdk.utils.extensions.noContent
import io.storiesteller.editor.model.EditState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

internal class NoteEditorViewModel(
    val storiesTellerManager: StoriesTellerManager,
    private val documentRepository: DocumentRepository,
    private val documentFilter: DocumentFilter = DocumentFilterObject,
) : ViewModel(),
    BackstackInform by storiesTellerManager,
    BackstackHandler by storiesTellerManager {

    private val _isEditableState = MutableStateFlow(true)

    /**
     * This property defines if the document should be edited (you can write in it, for example)
     */
    val isEditable: StateFlow<Boolean> = _isEditableState

    private val _showGlobalMenu = MutableStateFlow(false)
    val showGlobalMenu = _showGlobalMenu.asStateFlow()

    private val _editHeader = MutableStateFlow(false)
    val editHeader = _editHeader.asStateFlow()

    private val hasBackAction = combine(showGlobalMenu, editHeader) { globalMenu, headerEdit ->
        globalMenu || headerEdit
    }

    val isEditState: StateFlow<EditState> = storiesTellerManager.onEditPositions.map { set ->
        when {
            set.isNotEmpty() -> EditState.SELECTED_TEXT

            else -> EditState.TEXT
        }
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = EditState.TEXT)

    private val story: StateFlow<StoryState> = storiesTellerManager.currentStory
    val scrollToPosition = storiesTellerManager.scrollToPosition
    val toDraw = storiesTellerManager.toDraw.stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DrawState(emptyMap())
    )

    fun deleteSelection() {
        storiesTellerManager.deleteSelection()
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
        if (storiesTellerManager.isInitialized()) return

        storiesTellerManager.newStory(documentId, title)

        viewModelScope.launch {
            storiesTellerManager.currentDocument.stateIn(this).value?.let { document ->
                documentRepository.saveDocument(document)
                storiesTellerManager.saveOnStoryChanges()
            }
        }
    }

    fun requestDocumentContent(documentId: String) {
        if (storiesTellerManager.isInitialized()) return

        viewModelScope.launch(Dispatchers.IO) {
            val document = documentRepository.loadDocumentById(documentId)

            if (document != null) {
                storiesTellerManager.initDocument(document)
                storiesTellerManager.saveOnStoryChanges()
            }
        }
    }

    private fun removeNoteIfEmpty(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val document = storiesTellerManager.currentDocument.stateIn(this).value

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
        storiesTellerManager.currentStory.value.stories[0]?.let { storyStep ->
            val action = Action.StoryStateChange(
                storyStep = storyStep.copy(
                    decoration = Decoration(
                        backgroundColor = color
                    )
                ),
                position = 0
            )
            storiesTellerManager.changeStoryState(action)
        }
    }

    fun onHeaderEditionCancel() {
        _editHeader.value = false
    }

    fun onMoreOptionsClick() {
        _showGlobalMenu.value = !_showGlobalMenu.value
    }

    fun shareDocumentInJson(context: Context) {
        val json = storiesTellerJson

        viewModelScope.launch {
            val document: Document = storiesTellerManager.currentDocument
                .stateIn(viewModelScope)
                .value ?: return@launch

            val documentTitle = document.title.replace(" ", "_")

            val apiContent = documentFilter.removeTypesFromDocument(document.content)
                .map { (position, story) ->
                    story.toApi(position)
                }

            val request = document.toApi().copy(content = apiContent).wrapInRequest()
            val jsonDocument = json.encodeToString(request)

            val intent = Intent(Intent.ACTION_SEND).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(Intent.EXTRA_TEXT, jsonDocument)
                putExtra(Intent.EXTRA_TITLE, documentTitle)
                action = Intent.ACTION_SEND
                type = "application/json"
            }

            context.startActivity(
                Intent.createChooser(intent, "Export Document")
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        storiesTellerManager.onClear()
    }
}


package com.github.leandroborgesferreira.storytellerapp.editor

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.storyteller.backstack.BackstackHandler
import com.github.leandroborgesferreira.storyteller.backstack.BackstackInform
import com.github.leandroborgesferreira.storyteller.filter.DocumentFilter
import com.github.leandroborgesferreira.storyteller.filter.DocumentFilterObject
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.story.DrawState
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.models.story.Decoration
import com.github.leandroborgesferreira.storyteller.serialization.extensions.toApi
import com.github.leandroborgesferreira.storyteller.serialization.json.storyTellerJson
import com.github.leandroborgesferreira.storyteller.serialization.request.wrapInRequest
import com.github.leandroborgesferreira.storyteller.utils.extensions.noContent
import com.github.leandroborgesferreira.storytellerapp.editor.model.EditState
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
    val storyTellerManager: StoryTellerManager,
    private val documentRepository: DocumentRepository,
    private val documentFilter: DocumentFilter = DocumentFilterObject,
) : ViewModel(),
    BackstackInform by storyTellerManager,
    BackstackHandler by storyTellerManager {

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

    val isEditState: StateFlow<EditState> = storyTellerManager.onEditPositions.map { set ->
        when {
            set.isNotEmpty() -> EditState.SELECTED_TEXT

            else -> EditState.TEXT
        }
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = EditState.TEXT)

    private val story: StateFlow<StoryState> = storyTellerManager.currentStory
    val scrollToPosition = storyTellerManager.scrollToPosition
    val toDraw = storyTellerManager.toDraw.stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DrawState(emptyMap())
    )

    fun deleteSelection() {
        storyTellerManager.deleteSelection()
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
        if (storyTellerManager.isInitialized()) return

        storyTellerManager.newStory(documentId, title)

        viewModelScope.launch {
            storyTellerManager.currentDocument.stateIn(this).value?.let { document ->
                documentRepository.saveDocument(document)
                storyTellerManager.saveOnStoryChanges()
            }
        }
    }

    fun requestDocumentContent(documentId: String) {
        if (storyTellerManager.isInitialized()) return

        viewModelScope.launch(Dispatchers.IO) {
            val document = documentRepository.loadDocumentById(documentId)

            if (document != null) {
                storyTellerManager.initDocument(document)
                storyTellerManager.saveOnStoryChanges()
            }
        }
    }

    private fun removeNoteIfEmpty(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val document = storyTellerManager.currentDocument.stateIn(this).value

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
        storyTellerManager.currentStory.value.stories[0]?.let { storyStep ->
            val action = Action.StoryStateChange(
                storyStep = storyStep.copy(
                    decoration = Decoration(
                        backgroundColor = color
                    )
                ),
                position = 0
            )
            storyTellerManager.changeStoryState(action)
        }
    }

    fun onHeaderEditionCancel() {
        _editHeader.value = false
    }

    fun onMoreOptionsClick() {
        _showGlobalMenu.value = !_showGlobalMenu.value
    }

    fun shareDocumentInJson(context: Context) {
        val json = storyTellerJson

        viewModelScope.launch {
            val document: Document = storyTellerManager.currentDocument
                .stateIn(viewModelScope)
                .value ?: return@launch

            val documentTitle = document.title.replace(" ", "_")

            val apiContent = documentFilter.removeMetaData(document.content)
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
        storyTellerManager.onClear()
    }
}


package br.com.leandroferreira.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.storyteller.backstack.BackstackHandler
import com.github.leandroborgesferreira.storyteller.backstack.BackstackInform
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.model.story.DrawState
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
import com.github.leandroborgesferreira.storyteller.utils.extensions.noContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteEditorViewModel(
    val storyTellerManager: StoryTellerManager,
    private val documentRepository: DocumentRepository
) : ViewModel(),
    BackstackInform by storyTellerManager,
    BackstackHandler by storyTellerManager {

    private val _editModeState = MutableStateFlow(true)
    val editModeState: StateFlow<Boolean> = _editModeState

    val isEditState = storyTellerManager.positionsOnEdit.map { set ->
        set.isNotEmpty()
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = false)

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

    fun createNewDocument(documentId: String, title: String) {
        if (storyTellerManager.isInitialized()) return

        storyTellerManager.newStory(documentId, title)

        viewModelScope.launch {
            val document = storyTellerManager.currentDocument.stateIn(this).value

            documentRepository.saveDocument(document)
            storyTellerManager.saveOnStoryChanges(documentRepository)
        }
    }

    fun requestDocumentContent(documentId: String) {
        if (storyTellerManager.isInitialized()) return

        viewModelScope.launch(Dispatchers.IO) {
            val document = documentRepository.loadDocumentById(documentId)

            if (document != null) {
                storyTellerManager.initDocument(document)
                storyTellerManager.saveOnStoryChanges(documentRepository)
            }
        }
    }

    fun removeNoteIfEmpty(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val document = storyTellerManager.currentDocument.stateIn(this).value

            if (story.value.stories.noContent()) {
                documentRepository.deleteDocument(document)

                withContext(Dispatchers.Main) {
                    onComplete()
                }
            } else {
                withContext(Dispatchers.Main) {
                    onComplete()
                }
            }
        }
    }
}


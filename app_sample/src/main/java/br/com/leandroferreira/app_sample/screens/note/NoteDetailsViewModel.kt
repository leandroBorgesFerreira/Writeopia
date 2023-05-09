package br.com.leandroferreira.app_sample.screens.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.manager.StoryTellerManager
import br.com.leandroferreira.storyteller.model.story.StoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    val storyTellerManager: StoryTellerManager,
    private val storyDetailsRepository: StoryDetailsRepository
) : ViewModel() {

    private val _editModeState = MutableStateFlow(true)
    val editModeState: StateFlow<Boolean> = _editModeState

    val story: StateFlow<StoryState> = storyTellerManager.normalizedStepsState

    fun toggleEdit() {
        _editModeState.value = !_editModeState.value
    }

    fun requestDocumentContent(documentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val document = storyDetailsRepository.loadDocumentBy(documentId)
            val content = document?.content

            if (content != null) {
                val title = document.title //Todo: Use this later!
                storyTellerManager.addStories(content)
            }
        }
    }

    fun updateState() {
        storyTellerManager.updateState()
    }
}

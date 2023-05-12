package br.com.leandroferreira.app_sample.screens.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.manager.StoryTellerManager
import br.com.leandroferreira.storyteller.model.story.StoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoriesViewModel(
    val storyTellerManager: StoryTellerManager,
    private val repo: StoriesRepo
) : ViewModel() {

    private val _editModeState = MutableStateFlow(true)
    val editModeState: StateFlow<Boolean> = _editModeState

    val story: StateFlow<StoryState> = storyTellerManager.currentStory

    fun toggleEdit() {
        _editModeState.value = !_editModeState.value
    }

    fun requestStories() {
        viewModelScope.launch {
            storyTellerManager.addStories(repo.history())
        }
    }

    fun updateState() {
        storyTellerManager.updateState()
    }
}

package br.com.leandroferreira.app_sample.screens.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.storyteller.manager.StoryTellerManager
import com.github.leandroborgesferreira.storyteller.model.story.StoryState
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

    val scrollToPosition = storyTellerManager.scrollToPosition

    fun toggleEdit() {
        _editModeState.value = !_editModeState.value
    }

    fun requestStories() {
        viewModelScope.launch {
            storyTellerManager.initStories(repo.history())
        }
    }

    fun updateState() {
        storyTellerManager.updateState()
    }
}

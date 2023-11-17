package io.writeopia.note_menu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.preview.PreviewParser
import io.writeopia.auth.core.data.User
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.note_menu.data.usecase.NotesConfigurationRepository
import io.writeopia.note_menu.data.usecase.NotesUseCase
import io.writeopia.note_menu.extensions.toUiCard
import io.writeopia.note_menu.ui.dto.DocumentUi
import io.writeopia.utils_module.DISCONNECTED_USER_ID
import io.writeopia.utils_module.ResultData
import io.writeopia.utils_module.toBoolean
import io.writeopia.utils_module.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ChooseNoteViewModel(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: NotesConfigurationRepository,
    private val authManager: AuthManager,
    private val previewParser: PreviewParser = PreviewParser(),
) : ViewModel() {

    private var localUserId: String? = null

    private val _selectedNotes = MutableStateFlow(setOf<String>())
    val hasSelectedNotes = _selectedNotes.map { selectedIds ->
        selectedIds.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _documentsState: MutableStateFlow<ResultData<List<Document>>> =
        MutableStateFlow(ResultData.Idle())

    private val _user: MutableStateFlow<UserState<User>> = MutableStateFlow(UserState.Idle())
    val userName: StateFlow<UserState<String>> = _user.map { userState ->
        userState.map { user ->
            user.name
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, UserState.Idle())

    val documentsState: StateFlow<ResultData<List<DocumentUi>>> =
        combine(_selectedNotes, _documentsState) { selectedNoteIds, resultData ->
            resultData.map { documentList ->
                documentList.map { document ->
                    document.toUiCard(previewParser, selectedNoteIds.contains(document.id))
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, ResultData.Idle())

    private val _notesArrangement = MutableStateFlow<NotesArrangement?>(null)
    val notesArrangement = _notesArrangement.asStateFlow()

    private val _editState = MutableStateFlow(false)
    val editState = _editState.asStateFlow()

    private suspend fun getUserId(): String =
        localUserId ?: authManager.getUser().id.also { id ->
            localUserId = id
        }

    fun requestDocuments(force: Boolean) {
        if (documentsState.value !is ResultData.Complete || force) {
            viewModelScope.launch(Dispatchers.IO) {
                refreshNotes()
            }
        }
    }

    suspend fun requestUser() {
        try {
            _user.value = if (authManager.isLoggedIn().toBoolean()) {
                val user = authManager.getUser()

                if (user.id != DISCONNECTED_USER_ID) {
                    UserState.ConnectedUser(user)
                } else {
                    UserState.UserNotReturned()
                }
            } else {
                UserState.DisconnectedUser(User.disconnectedUser())
            }
        } catch (error: AuthException) {
            Log.d("ChooseNoteViewModel", "Error fetching user attributes. Error: $error")
        }
    }

    fun editMenu() {
        _editState.value = !editState.value
    }

    fun cancelMenu() {
        _editState.value = false
    }

    fun onDocumentSelected(id: String, selected: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedIds = _selectedNotes.value
            val newIds = if (selected) selectedIds + id else selectedIds - id

            _selectedNotes.value = newIds
        }
    }

    fun clearSelection() {
        _selectedNotes.value = emptySet()
    }

    fun listArrangementSelected() {
        viewModelScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.LIST)
            _notesArrangement.value = NotesArrangement.LIST
        }
    }

    fun gridArrangementSelected() {
        viewModelScope.launch {
            notesConfig.saveDocumentArrangementPref(NotesArrangement.GRID)
            _notesArrangement.value = NotesArrangement.GRID
        }
    }

    fun sortingSelected(orderBy: io.writeopia.sdk.persistence.core.sorting.OrderBy) {
        viewModelScope.launch(Dispatchers.IO) {
            notesConfig.saveDocumentSortingPref(orderBy)
            refreshNotes()
        }
    }

    fun copySelectedNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.duplicateDocuments(_selectedNotes.value.toList())
            clearSelection()
            refreshNotes()
        }
    }

    fun deleteSelectedNotes() {
        val selected = _selectedNotes.value

        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.deleteNotes(selected)
            clearSelection()
            refreshNotes()
        }
    }

    fun favoriteSelectedNotes() {

    }

    private suspend fun refreshNotes() {
        _documentsState.value = ResultData.Loading()

        try {
            val data = notesUseCase.loadDocumentsForUser(getUserId())
            _notesArrangement.value = NotesArrangement.fromString(notesConfig.arrangementPref())
            _documentsState.value = ResultData.Complete(data)
        } catch (e: Exception) {
            _documentsState.value = ResultData.Error(e)
        }
    }
}

sealed interface UserState<T> {
    class Idle<T> : UserState<T>
    class Loading<T> : UserState<T>
    class ConnectedUser<T>(val data: T) : UserState<T>
    class UserNotReturned<T> : UserState<T>
    class DisconnectedUser<T>(val data: T) : UserState<T>
}

fun <T, R> UserState<T>.map(fn: (T) -> R): UserState<R> =
    when (this) {
        is UserState.ConnectedUser -> UserState.ConnectedUser(fn(this.data))
        is UserState.Idle -> UserState.Idle()
        is UserState.Loading -> UserState.Loading()
        is UserState.DisconnectedUser -> UserState.DisconnectedUser(fn(this.data))
        is UserState.UserNotReturned -> UserState.UserNotReturned()
    }

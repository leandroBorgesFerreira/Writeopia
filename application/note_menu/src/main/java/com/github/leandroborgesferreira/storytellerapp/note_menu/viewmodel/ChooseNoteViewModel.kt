package com.github.leandroborgesferreira.storytellerapp.note_menu.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.kotlin.core.Amplify
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesConfigurationRepository
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase.NotesUseCase
import com.github.leandroborgesferreira.storytellerapp.note_menu.extensions.toUiCard
import com.github.leandroborgesferreira.storytellerapp.note_menu.ui.dto.DocumentUi
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import com.github.leandroborgesferreira.storytellerapp.utils_module.map
import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.preview.PreviewParser
import com.github.leandroborgesferreira.storyteller.persistence.sorting.OrderBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChooseNoteViewModel(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: NotesConfigurationRepository,
    private val previewParser: PreviewParser = PreviewParser(),
) : ViewModel() {

    private val _selectedNotes = MutableStateFlow(setOf<String>())
    val hasSelectedNotes = _selectedNotes.map { selectedIds ->
        selectedIds.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _isLogged = MutableStateFlow(true)
    val isLogged = _isLogged.asStateFlow()

    private val _documentsState: MutableStateFlow<ResultData<List<Document>>> =
        MutableStateFlow(ResultData.Idle())

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

    fun requestDocuments(force: Boolean) {
        if (documentsState.value !is ResultData.Complete || force) {
            viewModelScope.launch(Dispatchers.IO) {
                refreshNotes()
            }
        }
    }

    fun editMenu() {
        _editState.value = !editState.value
    }

    fun cancelMenu() {
        _editState.value = false
    }

    fun selectionListener(id: String, selected: Boolean) {
        val selectedIds = _selectedNotes.value
        val newIds = if (selected) selectedIds + id else selectedIds - id

        _selectedNotes.value = newIds
    }

    fun clearSelection() {
        _selectedNotes.value = emptySet()
    }

    fun addMockData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.mockData(context)



            val data = notesUseCase.loadDocuments()
            _documentsState.value = ResultData.Complete(data)
        }
    }

    fun listArrangementSelected() {
        notesConfig.saveDocumentArrangementPref(NotesArrangement.LIST)
        _notesArrangement.value = NotesArrangement.LIST
    }

    fun gridArrangementSelected() {
        notesConfig.saveDocumentArrangementPref(NotesArrangement.GRID)
        _notesArrangement.value = NotesArrangement.GRID
    }

    fun sortingSelected(orderBy: OrderBy) {
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

            _selectedNotes.value = emptySet()

            refreshNotes()
        }
    }

    fun favoriteSelectedNotes() {

    }

    fun logout() {
        viewModelScope.launch {
            when(val signOutResult = Amplify.Auth.signOut()) {
                is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                    // Sign Out completed fully and without errors.
                    Log.e("AuthQuickStart", "Logout!")
                    _isLogged.value = false
                }
                is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                    // Sign Out completed with some errors. User is signed out of the device.
                    signOutResult.hostedUIError?.let {
                        Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                        // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.

                    }
                    signOutResult.globalSignOutError?.let {
                        Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                        // Optional: Use escape hatch to retry revocation of it.accessToken.
                    }
                    signOutResult.revokeTokenError?.let {
                        Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                        // Optional: Use escape hatch to retry revocation of it.refreshToken.
                    }
                }
                is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                    // Sign Out failed with an exception, leaving the user signed in.
                    Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
                }
            }
        }
    }

    private suspend fun refreshNotes() {
        _documentsState.value = ResultData.Loading()

        try {
            val data = notesUseCase.loadDocuments()
            _notesArrangement.value = NotesArrangement.fromString(notesConfig.arrangementPref())
            _documentsState.value = ResultData.Complete(data)
        } catch (e: Exception) {
            _documentsState.value = ResultData.Error(e)
        }
    }
}

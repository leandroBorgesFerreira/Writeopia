package br.com.leandroferreira.note_menu.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.note_menu.data.usecase.NotesConfigurationRepository
import br.com.leandroferreira.utils.ResultData
import br.com.leandroferreira.note_menu.data.usecase.NotesUseCase
import br.com.leandroferreira.note_menu.extensions.toUiCard
import br.com.leandroferreira.note_menu.ui.dto.DocumentUi
import br.com.leandroferreira.utils.map
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.parse.PreviewParser
import com.github.leandroborgesferreira.storyteller.persistence.sorting.OrderBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChooseNoteViewModel(
    private val notesUseCase: NotesUseCase,
    private val notesConfig: NotesConfigurationRepository,
    private val previewParser: PreviewParser = PreviewParser()
) : ViewModel() {

    private val _selectedNotes = MutableStateFlow(setOf<String>())
    val hasSelectedNotes = _selectedNotes.map { selectedIds ->
        selectedIds.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _documentsState: MutableStateFlow<ResultData<List<Document>>> =
        MutableStateFlow(ResultData.Idle())

    @OptIn(ExperimentalCoroutinesApi::class)
    val documentsState: StateFlow<ResultData<List<DocumentUi>>> =
        _selectedNotes.flatMapLatest { selectedNoteIds ->
            _documentsState.map { resultData ->
                resultData.map { documentList ->
                    documentList.map { document ->
                        document.toUiCard(previewParser, selectedNoteIds.contains(document.id))
                    }
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
        notesUseCase.loadDocuments()
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

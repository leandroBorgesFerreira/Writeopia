package br.com.leandroferreira.note_menu.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.note_menu.data.usecase.NotesConfigurationRepository
import br.com.leandroferreira.utils.ResultData
import br.com.leandroferreira.note_menu.data.usecase.NotesUseCase
import br.com.leandroferreira.note_menu.extensions.toUiCard
import br.com.leandroferreira.note_menu.ui.dto.DocumentCard
import com.github.leandroborgesferreira.storyteller.parse.PreviewParser
import com.github.leandroborgesferreira.storyteller.persistence.sorting.OrderBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _documentsState: MutableStateFlow<ResultData<List<DocumentCard>>> =
        MutableStateFlow(ResultData.Idle())
    val documentsState: StateFlow<ResultData<List<DocumentCard>>> = _documentsState

    private val _notesArrangement = MutableStateFlow<NotesArrangement?>(null)
    val notesArrangement = _notesArrangement.asStateFlow()

    private val _editState = MutableStateFlow(false)
    val editState = _editState.asStateFlow()

    fun requestDocuments() {
        if (documentsState.value !is ResultData.Complete) {
            viewModelScope.launch(Dispatchers.IO) {
                refreshDocuments()
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

    private suspend fun refreshDocuments() {
        _documentsState.value = ResultData.Loading()

        try {
            val data = notesUseCase.loadDocuments()
                .map { document ->
                    document.toUiCard(previewParser)
                }

            _notesArrangement.value = NotesArrangement.fromString(notesConfig.arrangementPref())
            _documentsState.value = ResultData.Complete(data)
        } catch (e: Exception) {
            _documentsState.value = ResultData.Error(e)
        }
    }

    fun addMockData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.mockData(context)

            val data = notesUseCase.loadDocuments()
                .map { document ->
                    document.toUiCard(previewParser)
                }

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
        viewModelScope.launch {
            notesConfig.saveDocumentSortingPref(orderBy)
            refreshDocuments()
        }
    }
}

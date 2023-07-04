package br.com.leandroferreira.app_sample.screens.menu.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.app_sample.extensions.toUiCard
import br.com.leandroferreira.app_sample.screens.menu.NotesUseCase
import br.com.leandroferreira.app_sample.screens.menu.ui.dto.DocumentCard
import br.com.leandroferreira.app_sample.utils.ResultData
import com.github.leandroborgesferreira.storyteller.parse.PreviewParser
import com.github.leandroborgesferreira.storyteller.persistence.sorting.OrderBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChooseNoteViewModel(
    private val notesUseCase: NotesUseCase,
    private val previewParser: PreviewParser = PreviewParser()
) : ViewModel() {

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

    private suspend fun refreshDocuments() {
        _documentsState.value = ResultData.Loading()

        try {
            val data = notesUseCase.loadDocuments()
                .map { document ->
                    document.toUiCard(previewParser)
                }

            _notesArrangement.value = NotesArrangement.fromString(notesUseCase.arrangementPref())
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
        notesUseCase.saveDocumentArrangementPref(NotesArrangement.LIST)
        _notesArrangement.value = NotesArrangement.LIST
    }

    fun gridArrangementSelected() {
        notesUseCase.saveDocumentArrangementPref(NotesArrangement.GRID)
        _notesArrangement.value = NotesArrangement.GRID
    }

    fun sortingSelected(orderBy: OrderBy) {
        viewModelScope.launch {
            notesUseCase.saveDocumentSortingPref(orderBy)
            refreshDocuments()
        }
    }
}

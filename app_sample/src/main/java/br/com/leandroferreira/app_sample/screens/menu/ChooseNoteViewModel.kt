package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.storyteller.persistence.parse.toModel
import br.com.leandroferreira.app_sample.utils.ResultData
import br.com.leandroferreira.storyteller.model.document.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChooseNoteViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    private val _documentsState: MutableStateFlow<ResultData<List<Document>>> =
        MutableStateFlow(ResultData.Idle())

    val documentsState: StateFlow<ResultData<List<Document>>> = _documentsState

    fun requestDocuments() {
        if (documentsState.value !is ResultData.Complete) {
            viewModelScope.launch(Dispatchers.IO) {
                _documentsState.value = ResultData.Loading()

                try {
                    val data = notesRepository.loadDocuments()
                        .map { documentEntity -> documentEntity.toModel() }

                    _documentsState.value = ResultData.Complete(data)
                } catch (e: Exception) {
                    _documentsState.value = ResultData.Error(e)
                }
            }
        }
    }

    fun addMockData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.mockData(context)

            val data = notesRepository.loadDocuments()
                .map { documentEntity -> documentEntity.toModel() }

            _documentsState.value = ResultData.Complete(data)
        }
    }

}

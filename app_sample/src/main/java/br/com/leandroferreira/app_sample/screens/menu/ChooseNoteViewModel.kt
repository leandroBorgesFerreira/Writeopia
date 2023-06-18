package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandroferreira.app_sample.extensions.toUiCard
import br.com.leandroferreira.app_sample.screens.menu.ui.dto.DocumentCard
import com.github.leandroborgesferreira.storyteller.persistence.parse.toModel
import br.com.leandroferreira.app_sample.utils.ResultData
import com.github.leandroborgesferreira.storyteller.parse.PreviewParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChooseNoteViewModel(
    private val notesUseCase: NotesUseCase,
    private val previewParser: PreviewParser = PreviewParser()
) : ViewModel() {

    private val _documentsState: MutableStateFlow<ResultData<List<DocumentCard>>> =
        MutableStateFlow(ResultData.Idle())

    val documentsState: StateFlow<ResultData<List<DocumentCard>>> = _documentsState

    fun requestDocuments() {
        if (documentsState.value !is ResultData.Complete) {
            viewModelScope.launch(Dispatchers.IO) {
                _documentsState.value = ResultData.Loading()

                try {
                    val data = notesUseCase.loadDocuments()
                        .map { documentEntity ->
                            documentEntity.toUiCard(previewParser)
                        }

                    _documentsState.value = ResultData.Complete(data)
                } catch (e: Exception) {
                    _documentsState.value = ResultData.Error(e)
                }
            }
        }
    }

    fun addMockData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.mockData(context)

            val data = notesUseCase.loadDocuments()
                .map { documentEntity ->
                    documentEntity.toUiCard(previewParser)
                }

            _documentsState.value = ResultData.Complete(data)
        }
    }

}

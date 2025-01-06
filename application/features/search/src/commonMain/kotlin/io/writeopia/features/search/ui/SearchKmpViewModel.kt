package io.writeopia.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.writeopia.features.search.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SearchKmpViewModel(
    private val searchRepository: SearchRepository
) : SearchViewModel, ViewModel() {

    private val _searchState = MutableStateFlow("")
    override val searchState: StateFlow<String> = _searchState.asStateFlow()

    override val queryResults by lazy {
        searchState.map { query ->
            if (query.isEmpty()) {
                searchRepository.getNotesAndFolders()
            } else {
                searchRepository.searchNotesAndFolders(query)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    override fun init() {}

    override fun onSearchType(query: String) {
        _searchState.value = query
    }
}

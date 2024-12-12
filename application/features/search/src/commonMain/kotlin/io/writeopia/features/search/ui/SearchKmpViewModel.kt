package io.writeopia.features.search.ui

import io.writeopia.common.utils.KmpViewModel
import io.writeopia.features.search.repository.SearchItem
import io.writeopia.features.search.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchKmpViewModel(private val searchRepository: SearchRepository) : SearchViewModel, KmpViewModel() {

    private val _searchState = MutableStateFlow("")
    override val searchState: StateFlow<String> = _searchState.asStateFlow()

    private val _queryResults = MutableStateFlow<List<SearchItem>>(emptyList())
    override val queryResults = _queryResults.asStateFlow()

    override fun init() {
        _queryResults.value = searchRepository.getNotesAndFolders()
    }

    override fun onSearchType(query: String) {
        _searchState.value = query
    }
}

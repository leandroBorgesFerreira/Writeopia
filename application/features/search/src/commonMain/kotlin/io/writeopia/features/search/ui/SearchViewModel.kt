package io.writeopia.features.search.ui

import io.writeopia.features.search.repository.SearchItem
import kotlinx.coroutines.flow.StateFlow

interface SearchViewModel {
    val searchState: StateFlow<String>

    val queryResults: StateFlow<List<SearchItem>>

    fun onSearchType(query: String)

    fun init()
}

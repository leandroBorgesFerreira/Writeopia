package io.writeopia.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class AndroidSearchViewModel(
    private val searchKmpViewModel: SearchKmpViewModel
) : ViewModel(), SearchViewModel by searchKmpViewModel {

    init {
        searchKmpViewModel.initCoroutine(viewModelScope)
    }
}

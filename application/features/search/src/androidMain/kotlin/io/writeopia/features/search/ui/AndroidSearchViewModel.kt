package io.writeopia.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class AndroidSearchViewModel(
    private val searchKmpViewModel: SearchKmpViewModel
) : SearchViewModel by searchKmpViewModel

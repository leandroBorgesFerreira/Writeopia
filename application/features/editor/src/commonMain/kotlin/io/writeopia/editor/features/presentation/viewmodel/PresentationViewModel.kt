package io.writeopia.editor.features.presentation.viewmodel

import io.writeopia.sdk.presentation.model.SlidePage
import kotlinx.coroutines.flow.StateFlow

interface PresentationViewModel {

    val currentPage: StateFlow<Int>

    val slidesState: StateFlow<List<SlidePage>>

    fun previousPage()

    fun nextPage()

    fun loadDocument(id: String)
}

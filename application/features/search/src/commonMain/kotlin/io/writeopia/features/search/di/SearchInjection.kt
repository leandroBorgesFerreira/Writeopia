package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.features.search.ui.SearchViewModel

interface SearchInjection {

    fun provideViewModel(): SearchKmpViewModel
}

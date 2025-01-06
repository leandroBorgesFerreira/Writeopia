package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import io.writeopia.features.search.ui.SearchViewModel
import kotlinx.coroutines.CoroutineScope

interface SearchInjection {

    @Composable
    fun provideViewModel(): SearchViewModel

    @Composable
    fun provideViewModelMobile(): SearchViewModel
}

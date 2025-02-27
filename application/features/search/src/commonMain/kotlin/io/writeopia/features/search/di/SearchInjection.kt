package io.writeopia.features.search.di

import io.writeopia.features.search.ui.SearchKmpViewModel

interface SearchInjection {

    fun provideViewModel(): SearchKmpViewModel
}

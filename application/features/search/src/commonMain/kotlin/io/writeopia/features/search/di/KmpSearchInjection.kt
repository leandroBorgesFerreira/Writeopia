package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.features.search.repository.SearchRepository
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.models.search.FolderSearch
import io.writeopia.sdk.persistence.core.DocumentSearch
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.FolderSqlDelightDao

class KmpSearchInjection(private val writeopiaDb: WriteopiaDb? = null) : SearchInjection {

    private fun provideFolderSqlDelightDao() = FolderSqlDelightDao(writeopiaDb)

    private fun provideDocumentSqlDao() = DocumentSqlDao(
        writeopiaDb?.documentEntityQueries,
        writeopiaDb?.storyStepEntityQueries
    )

    fun provideRepository(
        folderDao: FolderSearch = provideFolderSqlDelightDao(),
        documentDao: DocumentSearch = provideDocumentSqlDao()
    ): SearchRepository = SearchRepository(folderDao, documentDao)

    @Composable
    override fun provideViewModel(): SearchKmpViewModel = viewModel {
        SearchKmpViewModel(searchRepository = provideRepository())
    }
}

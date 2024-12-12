package io.writeopia.features.search.di

import io.writeopia.features.search.repository.SearchRepository
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.features.search.ui.SearchViewModel
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.FolderSqlDelightDao
import kotlinx.coroutines.CoroutineScope

class SearchInjection(private val writeopiaDb: WriteopiaDb) {

    private fun provideFolderSqlDelightDao() = FolderSqlDelightDao(writeopiaDb)

    private fun provideDocumentSqlDao() = DocumentSqlDao(
        writeopiaDb.documentEntityQueries,
        writeopiaDb.storyStepEntityQueries
    )

    private fun provideRepository(
        folderDao: FolderSqlDelightDao = provideFolderSqlDelightDao(),
        documentSqlDao: DocumentSqlDao = provideDocumentSqlDao()
    ): SearchRepository = SearchRepository(folderDao, documentSqlDao)

    fun provideViewModel(
        coroutineScope: CoroutineScope? = null,
        searchRepository: SearchRepository = provideRepository()
    ): SearchViewModel =
        SearchKmpViewModel(searchRepository = searchRepository).apply {
            if (coroutineScope != null) {
                this.initCoroutine(coroutineScope)
            }
        }
}

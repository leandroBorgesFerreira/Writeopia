package io.writeopia.features.search.di

import androidx.compose.runtime.Composable
import io.writeopia.features.search.repository.SearchRepository
import io.writeopia.features.search.ui.SearchKmpViewModel
import io.writeopia.features.search.ui.SearchViewModel
import io.writeopia.models.search.FolderSearch
import io.writeopia.sdk.persistence.core.DocumentSearch
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.dao.FolderSqlDelightDao
import kotlinx.coroutines.CoroutineScope

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

    override fun provideViewModel(
        coroutineScope: CoroutineScope?
    ): SearchKmpViewModel =
        SearchKmpViewModel(searchRepository = provideRepository()).apply {
            if (coroutineScope != null) {
                this.initCoroutine(coroutineScope)
            }
        }

    @Composable
    override fun provideViewModelMobile(coroutineScope: CoroutineScope?): SearchViewModel {
        TODO("Not yet implemented")
    }
}

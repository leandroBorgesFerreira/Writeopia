package io.writeopia.sqldelight.di

import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.core.repository.InMemoryDocumentRepository
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentRepository
import io.writeopia.sql.WriteopiaDb

class SqlDelightDaoInjector(private val database: WriteopiaDb?) : RepositoryInjector {

    private val inMemoryDocumentRepository = InMemoryDocumentRepository()

    private fun provideDocumentSqlDao(): DocumentSqlDao? =
        database?.run {
            DocumentSqlDao(
                documentEntityQueries,
                storyStepEntityQueries
            )
        }


    override fun provideDocumentRepository(): DocumentRepository =
        provideDocumentSqlDao()?.let(::SqlDelightDocumentRepository)
            ?: inMemoryDocumentRepository

    companion object {
        fun noop() = SqlDelightDaoInjector(null)
    }
}

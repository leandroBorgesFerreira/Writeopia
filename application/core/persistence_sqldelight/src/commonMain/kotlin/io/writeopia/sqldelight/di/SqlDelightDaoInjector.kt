package io.writeopia.sqldelight.di

import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentRepository
import io.writeopia.sql.WriteopiaDb

class SqlDelightDaoInjector(private val database: WriteopiaDb) : RepositoryInjector {

    private fun provideDocumentSqlDao(): DocumentSqlDao = DocumentSqlDao(
        database.documentEntityQueries,
        database.storyStepEntityQueries
    )

    override fun provideDocumentRepository(): DocumentRepository =
        SqlDelightDocumentRepository(provideDocumentSqlDao())

    companion object
}

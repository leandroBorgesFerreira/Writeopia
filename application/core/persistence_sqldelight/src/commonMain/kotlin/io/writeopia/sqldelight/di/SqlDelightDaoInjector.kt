package io.writeopia.sqldelight.di

import io.writeopia.sdk.persistence.core.dao.DocumentRepository
import io.writeopia.sdk.persistence.core.di.RepositoryInjector
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentRepository
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.database.createDatabase
import io.writeopia.sqldelight.database.driver.DriverFactory

class SqlDelightDaoInjector(private val database: WriteopiaDb) : RepositoryInjector {

    private fun provideDocumentSqlDao(): DocumentSqlDao = DocumentSqlDao(
        database.documentEntityQueries,
        database.storyStepEntityQueries
    )

    override fun provideDocumentRepository(): DocumentRepository =
        SqlDelightDocumentRepository(provideDocumentSqlDao())

    companion object
}

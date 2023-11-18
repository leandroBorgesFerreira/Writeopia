package io.writeopia.sqldelight.di

import io.writeopia.sdk.persistence.core.dao.DocumentRepository
import io.writeopia.sdk.persistence.core.di.DaosInjector
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentRepository
import io.writeopia.sqldelight.database.createDatabase
import io.writeopia.sqldelight.database.driver.DriverFactory

class SqlDelightDaoInjector(driverFactory: DriverFactory) : DaosInjector {

    private val database = createDatabase(driverFactory)

    private fun provideDocumentSqlDao(): DocumentSqlDao = DocumentSqlDao(
        database.documentEntityQueries,
        database.storyStepEntityQueries
    )

    override fun provideDocumentDao(): DocumentRepository = SqlDelightDocumentRepository(provideDocumentSqlDao())

    companion object
}

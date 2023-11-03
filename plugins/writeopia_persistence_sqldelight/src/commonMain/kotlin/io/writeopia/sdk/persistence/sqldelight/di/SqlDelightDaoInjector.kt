package io.writeopia.sdk.persistence.sqldelight.di

import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.sdk.persistence.sqldelight.DriverFactory
import io.writeopia.sdk.persistence.sqldelight.createDatabase
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentDao

class SqlDelightDaoInjector(driverFactory: DriverFactory) {

    private val database = createDatabase(driverFactory)

    private fun provideDocumentSqlDao(): DocumentSqlDao = DocumentSqlDao(database)

    fun provideDocumentDao(
        documentSqlDao: DocumentSqlDao = provideDocumentSqlDao()
    ): DocumentDao = SqlDelightDocumentDao(documentSqlDao)
}
package io.writeopia.sdk.persistence.sqldelight.di

import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.sdk.persistence.core.di.DaosInjector
import io.writeopia.sdk.persistence.sqldelight.DriverFactory
import io.writeopia.sdk.persistence.sqldelight.createDatabase
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentDao

class SqlDelightDaoInjector(driverFactory: DriverFactory): DaosInjector {

    private val database = createDatabase(driverFactory)

    private fun provideDocumentSqlDao(): DocumentSqlDao = DocumentSqlDao(database)

    override fun provideDocumentDao(): DocumentDao = SqlDelightDocumentDao(provideDocumentSqlDao())
}
package io.writeopia.sqldelight.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.writeopia.libraries.dbtests.DocumentRepositoryTests
import io.writeopia.sqldelight.database.DatabaseFactory.createDatabase
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test

class DocumentSqlDelightRepositoryTest {

    private lateinit var documentRepositoryTests: DocumentRepositoryTests

    private suspend fun createDb() {
        val database = createDatabase(DriverFactory(), JdbcSqliteDriver.IN_MEMORY)
        val documentRepository = SqlDelightDaoInjector(database).provideDocumentRepository()
        documentRepositoryTests = DocumentRepositoryTests(documentRepository)
    }

    @Test
    fun saveAndLoadADocumentWithoutContent() = runTest {
        createDb()
        documentRepositoryTests.saveAndLoadADocumentWithoutContent()
    }

    @Test
    fun saveAndLoadADocumentWithContent() = runTest {
        createDb()
        documentRepositoryTests.saveAndLoadADocumentWithContent()
    }

    @Test
    fun savingAndLoadingDocumentWithOneImageInRepository() = runTest {
        createDb()
        documentRepositoryTests.savingAndLoadingDocumentWithOneImageInRepository()
    }

    @Test
    fun savingAndLoadingDocumentWithManyImagesInRepository() = runTest {
        createDb()
        documentRepositoryTests.savingAndLoadingDocumentWithManyImagesInRepository()
    }

    @Test
    @Ignore("Step inside step persistence is currently not supported")
    fun savingAndLoadingDocumentOneImageGroupInRepository() = runTest {
        createDb()
        documentRepositoryTests.savingAndLoadingDocumentOneImageGroupInRepository()
    }
}

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

    private suspend fun createDb(): DocumentRepositoryTests {
        val database = createDatabase(DriverFactory(), JdbcSqliteDriver.IN_MEMORY)
        val documentRepository = SqlDelightDaoInjector(database).provideDocumentRepository()
        return DocumentRepositoryTests(documentRepository)
    }

    @Test
    @Ignore("Some error in sqldelight")
    fun saveAndLoadADocumentWithoutContent() = runTest {
        val documentRepositoryTests: DocumentRepositoryTests = createDb()
        documentRepositoryTests.saveAndLoadADocumentWithoutContent()
    }

    @Test
    @Ignore("Some error in sqldelight")
    fun saveAndLoadADocumentWithContent() = runTest {
        val documentRepositoryTests: DocumentRepositoryTests = createDb()
        documentRepositoryTests.saveAndLoadADocumentWithContent()
    }

    @Test
    @Ignore("Some error in sqldelight")
    fun savingAndLoadingDocumentWithOneImageInRepository() = runTest {
        val documentRepositoryTests: DocumentRepositoryTests = createDb()
        documentRepositoryTests.savingAndLoadingDocumentWithOneImageInRepository()
    }

    @Test
    @Ignore("Some error in sqldelight")
    fun savingAndLoadingDocumentWithManyImagesInRepository() = runTest {
        val documentRepositoryTests: DocumentRepositoryTests = createDb()
        documentRepositoryTests.savingAndLoadingDocumentWithManyImagesInRepository()
    }

    @Test
    @Ignore("Step inside step persistence is currently not supported")
    fun savingAndLoadingDocumentOneImageGroupInRepository() = runTest {
        val documentRepositoryTests: DocumentRepositoryTests = createDb()
        documentRepositoryTests.savingAndLoadingDocumentOneImageGroupInRepository()
    }
}

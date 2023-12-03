package io.writeopia.sqldelight.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.writeopia.libraries.dbtests.DocumentRepositoryTests
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test

class DocumentSqlDelightRepositoryTest {

    private lateinit var documentRepositoryTests: DocumentRepositoryTests

    @BeforeTest
    fun createDb() {
        val database = createDatabase(DriverFactory(), JdbcSqliteDriver.IN_MEMORY)
        val documentRepository = SqlDelightDaoInjector(database).provideDocumentRepository()
        documentRepositoryTests = DocumentRepositoryTests(documentRepository)
    }

    @Test
    fun saveAndLoadADocumentWithoutContent() = runTest {
        documentRepositoryTests.saveAndLoadADocumentWithoutContent()
    }

    @Test
    fun saveAndLoadADocumentWithContent() = runTest {
        documentRepositoryTests.saveAndLoadADocumentWithContent()
    }

    @Test
    fun savingAndLoadingDocumentWithOneImageInRepository() = runTest {
        documentRepositoryTests.savingAndLoadingDocumentWithOneImageInRepository()
    }

    @Test
    fun savingAndLoadingDocumentWithManyImagesInRepository() = runTest {
        documentRepositoryTests.savingAndLoadingDocumentWithManyImagesInRepository()
    }

    @Test
    @Ignore("Step inside step persistence is currently not supported")
    fun savingAndLoadingDocumentOneImageGroupInRepository() = runTest {
        documentRepositoryTests.savingAndLoadingDocumentOneImageGroupInRepository()
    }
}

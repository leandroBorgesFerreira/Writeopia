package io.writeopia.notemenu.viewmodel

import io.writeopia.notemenu.data.usecase.NotesUseCase
import io.writeopia.notemenu.di.NotesInjector
import io.writeopia.sdk.models.document.Document
import io.writeopia.sqldelight.database.DatabaseFactory
import io.writeopia.sqldelight.database.driver.DriverFactory
import io.writeopia.sqldelight.di.SqlDelightDaoInjector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertTrue

class NotesUseCaseTest {

    @Test
    fun blah() = runTest {
        val database = DatabaseFactory.createDatabase(DriverFactory())

        val injector = SqlDelightDaoInjector(database)
        val notesInjector = NotesInjector(database)

        val documentRepo = injector.provideDocumentRepository()
        val notesConfig = notesInjector.provideNotesConfigurationRepository()
        val folderRepository = notesInjector.provideFoldersRepository()

        val useCase = NotesUseCase.singleton(
            documentRepository = documentRepo,
            folderRepository = folderRepository,
            notesConfig = notesConfig
        )

        val now = Clock.System.now()

        useCase.saveDocument(
            Document(
                title = "document1",
                createdAt = now,
                lastUpdatedAt = now,
                userId = "disconnected_user",
                parentId = "root"
            )
        )

        val result = useCase.listenForMenuItemsByParentId("root", "disconnected_user").first()

        assertTrue { result.isNotEmpty() }
    }
}

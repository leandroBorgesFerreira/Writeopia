package io.writeopia.api.editor

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import io.writeopia.sdk.persistence.sqldelight.dao.sql.SqlDelightDocumentRepository
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.extensions.toApi
import io.writeopia.sdk.serialization.extensions.toModel
import io.writeopia.sdk.sql.WriteopiaDb
import kotlinx.datetime.Clock
import java.util.Properties

class WriteopiaEditorApi(
    private val documentRepository: DocumentRepository
) {

    fun introNotes(): List<DocumentApi> {
        val title = "Connected!"
        val now = Clock.System.now()

        val documentList = listOf(
            StoryStep(type = StoryTypes.TITLE.type, text = title),
            StoryStep(type = StoryTypes.TEXT.type, text = "You're now connected. Enjoy the app!!"),
        )

        return DocumentApi(
            id = "welcomeNote",
            title = title,
            content = documentList.mapIndexed { i, storyStep -> storyStep.toApi(i) },
            createdAt = now.toEpochMilliseconds(),
            lastUpdatedAt = now.toEpochMilliseconds(),
            userId = "null",
        ).let(::listOf)
    }

    fun example(): DocumentApi {
        val title = "Example Document"
        val now = Clock.System.now()

        val documentList = listOf(
            StoryStep(type = StoryTypes.TITLE.type, text = title),
            StoryStep(type = StoryTypes.TEXT.type, text = "sample message"),
        )

        return DocumentApi(
            id = "document_123",
            title = title,
            content = documentList.mapIndexed { i, storyStep -> storyStep.toApi(i) },
            createdAt = now.toEpochMilliseconds(),
            lastUpdatedAt = now.toEpochMilliseconds(),
            userId = "user_123",
        )
    }

    suspend fun saveDocument(document: DocumentApi) {
        documentRepository.saveDocument(document.toModel())
    }

    suspend fun getDocument(id: String): DocumentApi? =
        documentRepository.loadDocumentById(id)?.toApi()

    companion object {
        private fun createDatabase(
            url: String = JdbcSqliteDriver.IN_MEMORY
        ): WriteopiaDb {
            val driver = JdbcSqliteDriver(url, Properties(), WriteopiaDb.Schema)
            return WriteopiaDb(driver)
        }

        fun create(): WriteopiaEditorApi {
            val database: WriteopiaDb = createDatabase()
            val documentSqlDao = DocumentSqlDao(
                database.documentEntityQueries,
                database.storyStepEntityQueries
            )
            val documentRepository = SqlDelightDocumentRepository(documentSqlDao)
            return WriteopiaEditorApi(documentRepository)
        }
    }
}
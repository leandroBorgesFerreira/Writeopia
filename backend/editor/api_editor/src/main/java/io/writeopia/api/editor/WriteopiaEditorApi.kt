package io.writeopia.api.editor

import io.writeopia.api.utils.example
import io.writeopia.database.connection.SqlDelightJdbcConnection
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

    fun example(): DocumentApi = DocumentApi.example()

    suspend fun saveDocument(document: DocumentApi) {
        documentRepository.saveDocument(document.toModel())
    }

    suspend fun getDocument(id: String): DocumentApi? =
        documentRepository.loadDocumentById(id)?.toApi()

    companion object {
        private fun createDatabase(
            inMemory: Boolean = false
        ): WriteopiaDb {
            val driver = if (inMemory) {
                SqlDelightJdbcConnection.inMemory()
            } else {
                SqlDelightJdbcConnection.jdbcDriver(System.getenv("DB_USER"))
            }

            return WriteopiaDb(driver)
        }

        fun create(inMemory: Boolean = false): WriteopiaEditorApi {
            val database: WriteopiaDb = createDatabase(inMemory)
            val documentSqlDao = DocumentSqlDao(
                database.documentEntityQueries,
                database.storyStepEntityQueries
            )
            val documentRepository = SqlDelightDocumentRepository(documentSqlDao)
            return WriteopiaEditorApi(documentRepository)
        }
    }
}
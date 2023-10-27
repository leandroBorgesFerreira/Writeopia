package io.writeopia.sdk.persistence.sqldelight.dao

import app.cash.sqldelight.db.SqlDriver
import io.writeopia.sdk.models.document.Document
import io.writeopia.sql.DocumentEntityQueries
import io.writeopia.sql.StoryStepEntityQueries
import io.writeopia.sql.WriteopiaDb
import kotlinx.datetime.Instant

class DocumentSqlDao(driver: SqlDriver) {

    private val database: WriteopiaDb = WriteopiaDb(driver)
    private val documentQueries: DocumentEntityQueries = database.documentEntityQueries
    private val storyStepQueries: StoryStepEntityQueries = database.storyStepEntityQueries

    suspend fun loadDocumentsWithContent(): List<Document> {
        return documentQueries.selectAll { id, title, createdAt, lastUpdatedAt, userId ->
            Document(
                id = id,
                title = title,
                content = emptyMap(),
                createdAt = Instant.fromEpochMilliseconds(createdAt),
                lastUpdatedAt = Instant.fromEpochMilliseconds(lastUpdatedAt),
                userId = userId,
            )
        }.executeAsList()
            .map { document ->
                val storyStepList = storyStepQueries.selectByDocumentId(document.id).executeAsList()


                document.copy()
            }
    }
}

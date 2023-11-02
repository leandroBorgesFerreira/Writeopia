package io.writeopia.sdk.persistence.sqldelight.dao

import app.cash.sqldelight.db.SqlDriver
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.persistence.sqldelight.mapper.toModel
import io.writeopia.sql.DocumentEntityQueries
import io.writeopia.sql.StoryStepEntityQueries
import io.writeopia.sql.WriteopiaDb
import kotlinx.datetime.Instant

class DocumentSqlDao(driver: SqlDriver) {

    private val database: WriteopiaDb = WriteopiaDb(driver)
    private val documentQueries: DocumentEntityQueries = database.documentEntityQueries
    private val storyStepQueries: StoryStepEntityQueries = database.storyStepEntityQueries

    suspend fun insertDocument(document: Document) {
        document.content.values.forEachIndexed { i, storyStep ->
            storyStep.run {
                storyStepQueries.insert(
                    id = id,
                    local_id = localId,
                    type = type.number.toLong(),
                    parent_id = parentId,
                    url = url,
                    path = path,
                    text = text,
                    checked = checked.let { if (it == true) 1 else 0 },
                    position = i.toLong(),
                    document_id = document.id,
                    is_group = isGroup.let { if (it) 1 else 0 },
                    has_inner_steps = steps.isNotEmpty().let { if (it) 1 else 0 },
                    background_color = decoration.backgroundColor?.toLong(),
                )
            }
        }

        documentQueries.insert(
            id = document.id,
            title = document.title,
            created_at = document.createdAt.toEpochMilliseconds(),
            last_updated_at = document.lastUpdatedAt.toEpochMilliseconds(),
            user_id = document.userId,
        )
    }

    suspend fun insertDocuments(vararg documents: Document) {
        documents.forEach { document ->
            insertDocument(document)
        }
    }

    suspend fun loadDocumentById(id: String): Document? =
        documentQueries.selectById(id).executeAsOneOrNull()?.toModel()

    suspend fun loadDocumentsWithContent(): List<Document> {
        val result = documentQueries.selectWithContent()
            .executeAsList()
            .groupBy { it.document_id }
            .mapNotNull { (documentId, content) ->
                StoryTypes.LAST_SPACE

                content.firstOrNull()?.run {
                    Document(
                        id = documentId,
                        title = title,
                        content = content.associate { innerContent ->
                            val storyStep = StoryStep(
                                id = id_,
                                localId = local_id,
                                type = StoryTypes.fromNumber(type.toInt()).type,
                                parentId = parent_id,
                                url = url,
                                path = path,
                                text = text,
                                checked = checked == 1L,
//                                steps = emptyList(), // Todo: Fix!
//                                decoration = decoration, // Todo: Fix!
                            )

                            innerContent.position.toInt() to storyStep
                        },
                        createdAt = Instant.fromEpochMilliseconds(created_at),
                        lastUpdatedAt = Instant.fromEpochMilliseconds(last_updated_at),
                        userId = user_id,
                    )
                }
            }

        return result
    }

    suspend fun deleteDocument(document: Document) {
        documentQueries.delete(document.id)
    }

    suspend fun loadDocumentWithContentById(
        documentId: String
    ): List<Document> =
        // Todo: Add documentId
        loadDocumentsWithContent()

    suspend fun deleteDocumentsByUserId(userId: String) {
        documentQueries.deleteByUserId(userId)
    }

    suspend fun deleteDocuments(vararg documents: Document) {
        documents.forEach { document ->
            deleteDocument(document)
        }
    }
}

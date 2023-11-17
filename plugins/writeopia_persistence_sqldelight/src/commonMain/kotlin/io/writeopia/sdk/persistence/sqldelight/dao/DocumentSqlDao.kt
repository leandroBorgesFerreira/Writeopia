package io.writeopia.sdk.persistence.sqldelight.dao

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.sql.DocumentEntityQueries
import io.writeopia.sdk.sql.StoryStepEntityQueries
import io.writeopia.sdk.sql.WriteopiaDb
import kotlinx.datetime.Instant

class DocumentSqlDao(database: WriteopiaDb) {
    private val documentQueries: DocumentEntityQueries = database.documentEntityQueries
    private val storyStepQueries: StoryStepEntityQueries = database.storyStepEntityQueries

    fun insertDocumentWithContent(document: Document) {
        document.content.values.forEachIndexed { i, storyStep ->
            insertStoryStep(storyStep, i.toLong(), document.id)
        }

        insertDocument(document)
    }

    fun insertDocument(document: Document) {
        documentQueries.insert(id = document.id,
            title = document.title,
            created_at = document.createdAt.toEpochMilliseconds(),
            last_updated_at = document.lastUpdatedAt.toEpochMilliseconds(),
            user_id = document.userId
        )
    }

    fun insertStoryStep(storyStep: StoryStep, position: Long, documentId: String) {
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
                position = position,
                document_id = documentId,
                is_group = isGroup.let { if (it) 1 else 0 },
                has_inner_steps = steps.isNotEmpty().let { if (it) 1 else 0 },
                background_color = decoration.backgroundColor?.toLong(),
            )
        }
    }

    fun insertDocuments(vararg documents: Document) {
        documents.forEach { document ->
            insertDocumentWithContent(document)
        }
    }

    fun loadDocumentWithContentByIds(id: List<String>): List<Document> =
        documentQueries.selectWithContentByIds(id)
            .executeAsList()
            .groupBy { it.document_id }
            .mapNotNull { (documentId, content) ->
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

    fun loadDocumentsWithContentByIds(ids: Set<String>): List<Document> {
        val result = documentQueries.selectWithContentByIds(ids)
            .executeAsList()
            .groupBy { it.document_id }
            .mapNotNull { (documentId, content) ->
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

    fun loadDocumentsWithContentByUserId(userId: String): List<Document> {
        return documentQueries.selectWithContentByUserId(userId).executeAsList()
            .groupBy { it.document_id }
            .mapNotNull { (documentId, content) ->
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
    }

    fun deleteDocumentById(document: String) {
        documentQueries.delete(document)
    }

    fun deleteDocumentByIds(ids: Set<String>) {
        documentQueries.deleteByIds(ids)
    }

    fun loadDocumentWithContentById(documentId: String): Document? =
        documentQueries.selectWithContentById(documentId).executeAsList()
            .groupBy { it.document_id }
            .mapNotNull { (documentId, content) ->
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
            .firstOrNull()

    fun deleteDocumentsByUserId(userId: String) {
        documentQueries.deleteByUserId(userId)
    }
}

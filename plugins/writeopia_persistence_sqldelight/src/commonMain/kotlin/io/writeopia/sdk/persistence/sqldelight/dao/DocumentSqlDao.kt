package io.writeopia.sdk.persistence.sqldelight.dao

import app.cash.sqldelight.async.coroutines.awaitAsList
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.persistence.core.extensions.sortWithOrderBy
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sdk.persistence.sqldelight.toLong
import io.writeopia.sdk.sql.DocumentEntityQueries
import io.writeopia.sdk.sql.StoryStepEntityQueries
import kotlinx.datetime.Instant

class DocumentSqlDao(
    private val documentQueries: DocumentEntityQueries?,
    private val storyStepQueries: StoryStepEntityQueries?,
) {

    suspend fun insertDocumentWithContent(document: Document) {
        document.content.values.forEachIndexed { i, storyStep ->
            insertStoryStep(storyStep, i.toLong(), document.id)
        }

        insertDocument(document)
    }

    suspend fun insertDocument(document: Document) {
        documentQueries?.insert(
            id = document.id,
            title = document.title,
            created_at = document.createdAt.toEpochMilliseconds(),
            last_updated_at = document.lastUpdatedAt.toEpochMilliseconds(),
            user_id = document.userId,
            favorite = document.favorite.toLong(),
            parent_document_id = document.parentId
        )
    }

    suspend fun insertStoryStep(storyStep: StoryStep, position: Long, documentId: String) {
        storyStep.run {
            storyStepQueries?.insert(
                id = id,
                local_id = localId,
                type = type.number.toLong(),
                parent_id = parentId,
                url = url,
                path = path,
                text = text,
                checked = checked.toLong(),
                position = position,
                document_id = documentId,
                is_group = isGroup.toLong(),
                has_inner_steps = steps.isNotEmpty().toLong(),
                background_color = decoration.backgroundColor?.toLong(),
            )
        }
    }

    suspend fun insertDocuments(vararg documents: Document) {
        documents.forEach { document ->
            insertDocumentWithContent(document)
        }
    }

    suspend fun loadDocumentWithContentByIds(id: List<String>): List<Document> =
        documentQueries?.selectWithContentByIds(id)
            ?.awaitAsList()
            ?.groupBy { it.id }
            ?.mapNotNull { (documentId, content) ->
                content.firstOrNull()?.let { document ->
                    val innerContent = content.filter { innerContent ->
                        !innerContent.id_.isNullOrEmpty()
                    }.associate { innerContent ->
                        val storyStep = StoryStep(
                            id = innerContent.id_!!,
                            localId = innerContent.local_id!!,
                            type = StoryTypes.fromNumber(innerContent.type!!.toInt()).type,
                            parentId = innerContent.parent_id,
                            url = innerContent.url,
                            path = innerContent.path,
                            text = innerContent.text,
                            checked = innerContent.checked == 1L,
//                                steps = emptyList(), // Todo: Fix!
//                                decoration = decoration, // Todo: Fix!
                        )

                        innerContent.position!!.toInt() to storyStep
                    }

                    Document(
                        id = documentId,
                        title = document.title,
                        content = innerContent,
                        createdAt = Instant.fromEpochMilliseconds(document.created_at),
                        lastUpdatedAt = Instant.fromEpochMilliseconds(document.last_updated_at),
                        userId = document.user_id,
                        favorite = document.favorite == 1L,
                        parentId = document.parent_document_id,
                    )
                }
            } ?: emptyList()

    suspend fun loadDocumentsWithContentByUserId(orderBy: String, userId: String): List<Document> {
        return documentQueries?.selectWithContentByUserId(userId)
            ?.awaitAsList()
            ?.groupBy { it.id }
            ?.mapNotNull { (documentId, content) ->
                content.firstOrNull()?.let { document ->
                    val innerContent = content.filter { innerContent ->
                        !innerContent.id_.isNullOrEmpty()
                    }.associate { innerContent ->
                        val storyStep = StoryStep(
                            id = innerContent.id_!!,
                            localId = innerContent.local_id!!,
                            type = StoryTypes.fromNumber(innerContent.type!!.toInt()).type,
                            parentId = innerContent.parent_id,
                            url = innerContent.url,
                            path = innerContent.path,
                            text = innerContent.text,
                            checked = innerContent.checked == 1L,
//                                steps = emptyList(), // Todo: Fix!
//                                decoration = decoration, // Todo: Fix!
                        )

                        innerContent.position!!.toInt() to storyStep
                    }

                    Document(
                        id = documentId,
                        title = document.title,
                        content = innerContent,
                        createdAt = Instant.fromEpochMilliseconds(document.created_at),
                        lastUpdatedAt = Instant.fromEpochMilliseconds(document.last_updated_at),
                        userId = document.user_id,
                        favorite = document.favorite == 1L,
                        parentId = document.parent_document_id,

                    )
                }
            }
            ?.sortWithOrderBy(OrderBy.fromString(orderBy))
            ?: emptyList()
    }

    suspend fun loadFavDocumentsWithContentByUserId(orderBy: String, userId: String): List<Document> {
        return documentQueries?.selectFavoritesWithContentByUserId(userId)
            ?.awaitAsList()
            ?.groupBy { it.id }
            ?.mapNotNull { (documentId, content) ->
                content.firstOrNull()?.let { document ->
                    val innerContent = content.filter { innerContent ->
                        !innerContent.id_.isNullOrEmpty()
                    }.associate { innerContent ->
                        val storyStep = StoryStep(
                            id = innerContent.id_!!,
                            localId = innerContent.local_id!!,
                            type = StoryTypes.fromNumber(innerContent.type!!.toInt()).type,
                            parentId = innerContent.parent_id,
                            url = innerContent.url,
                            path = innerContent.path,
                            text = innerContent.text,
                            checked = innerContent.checked == 1L,
//                                steps = emptyList(), // Todo: Fix!
//                                decoration = decoration, // Todo: Fix!
                        )

                        innerContent.position!!.toInt() to storyStep
                    }

                    Document(
                        id = documentId,
                        title = document.title,
                        content = innerContent,
                        createdAt = Instant.fromEpochMilliseconds(document.created_at),
                        lastUpdatedAt = Instant.fromEpochMilliseconds(document.last_updated_at),
                        userId = document.user_id,
                        favorite = document.favorite == 1L,
                        parentId = document.parent_document_id,
                    )
                }
            }
            ?.sortWithOrderBy(OrderBy.fromString(orderBy))
            ?: emptyList()
    }

    suspend fun loadDocumentsWithContentByUserIdAfterTime(
        userId: String,
        time: Long
    ): List<Document> {
        return documentQueries?.selectWithContentByUserIdAfterTime(userId, time)
            ?.awaitAsList()
            ?.groupBy { it.id }
            ?.mapNotNull { (documentId, content) ->
                content.firstOrNull()?.let { document ->
                    val innerContent = content.filter { innerContent ->
                        !innerContent.id_.isNullOrEmpty()
                    }.associate { innerContent ->
                        val storyStep = StoryStep(
                            id = innerContent.id_!!,
                            localId = innerContent.local_id!!,
                            type = StoryTypes.fromNumber(innerContent.type!!.toInt()).type,
                            parentId = innerContent.parent_id,
                            url = innerContent.url,
                            path = innerContent.path,
                            text = innerContent.text,
                            checked = innerContent.checked == 1L,
//                                steps = emptyList(), // Todo: Fix!
//                                decoration = decoration, // Todo: Fix!
                        )

                        innerContent.position!!.toInt() to storyStep
                    }

                    Document(
                        id = documentId,
                        title = document.title,
                        content = innerContent,
                        createdAt = Instant.fromEpochMilliseconds(document.created_at),
                        lastUpdatedAt = Instant.fromEpochMilliseconds(document.last_updated_at),
                        userId = document.user_id,
                        favorite = document.favorite == 1L,
                        parentId = document.parent_document_id,
                    )
                }
            } ?: emptyList()
    }

    suspend fun deleteDocumentById(document: String) {
        documentQueries?.delete(document)
    }

    suspend fun deleteDocumentByIds(ids: Set<String>) {
        documentQueries?.deleteByIds(ids)
    }

    suspend fun loadDocumentWithContentById(documentId: String): Document? =
        documentQueries?.selectWithContentById(documentId)
            ?.awaitAsList()
            ?.groupBy { it.id }
            ?.mapNotNull { (documentId, content) ->
                content.firstOrNull()?.let { document ->
                    val innerContent = content.filter { innerContent ->
                        !innerContent.id_.isNullOrEmpty()
                    }.associate { innerContent ->
                        val storyStep = StoryStep(
                            id = innerContent.id_!!,
                            localId = innerContent.local_id!!,
                            type = StoryTypes.fromNumber(innerContent.type!!.toInt()).type,
                            parentId = innerContent.parent_id,
                            url = innerContent.url,
                            path = innerContent.path,
                            text = innerContent.text,
                            checked = innerContent.checked == 1L,
//                                steps = emptyList(), // Todo: Fix!
//                                decoration = decoration, // Todo: Fix!
                        )

                        innerContent.position!!.toInt() to storyStep
                    }

                    Document(
                        id = documentId,
                        title = document.title,
                        content = innerContent,
                        createdAt = Instant.fromEpochMilliseconds(document.created_at),
                        lastUpdatedAt = Instant.fromEpochMilliseconds(document.last_updated_at),
                        userId = document.user_id,
                        favorite = document.favorite == 1L,
                        parentId = document.parent_document_id,
                    )
                }
            }
            ?.firstOrNull()

    suspend fun loadDocumentByParentId(parentId: String): List<Document> {
        return documentQueries?.selectWithContentByParentId(parentId)
            ?.awaitAsList()
            ?.groupBy { it.id }
            ?.mapNotNull { (documentId, content) ->
                content.firstOrNull()?.let { document ->
                    val innerContent = content.filter { innerContent ->
                        !innerContent.id_.isNullOrEmpty()
                    }.associate { innerContent ->
                        val storyStep = StoryStep(
                            id = innerContent.id_!!,
                            localId = innerContent.local_id!!,
                            type = StoryTypes.fromNumber(innerContent.type!!.toInt()).type,
                            parentId = innerContent.parent_id,
                            url = innerContent.url,
                            path = innerContent.path,
                            text = innerContent.text,
                            checked = innerContent.checked == 1L,
//                                steps = emptyList(), // Todo: Fix!
//                                decoration = decoration, // Todo: Fix!
                        )

                        innerContent.position!!.toInt() to storyStep
                    }

                    Document(
                        id = documentId,
                        title = document.title,
                        content = innerContent,
                        createdAt = Instant.fromEpochMilliseconds(document.created_at),
                        lastUpdatedAt = Instant.fromEpochMilliseconds(document.last_updated_at),
                        userId = document.user_id,
                        favorite = document.favorite == 1L,
                        parentId = document.parent_document_id,
                    )
                }
            } ?: emptyList()
    }

    suspend fun deleteDocumentsByUserId(userId: String) {
        documentQueries?.deleteByUserId(userId)
    }

    suspend fun deleteDocumentsByFolderId(folderId: String) {
        documentQueries?.deleteByFolderId(folderId)
    }

    suspend fun favoriteById(documentId: String) {
        documentQueries?.favoriteById(documentId)
    }

    suspend fun unFavoriteById(documentId: String) {
        documentQueries?.unFavoriteById(documentId)
    }
}



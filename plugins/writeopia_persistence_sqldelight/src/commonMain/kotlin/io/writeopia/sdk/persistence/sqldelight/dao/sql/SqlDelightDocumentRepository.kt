package io.writeopia.sdk.persistence.sqldelight.dao.sql

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

class SqlDelightDocumentRepository(
    private val documentSqlDao: DocumentSqlDao
) : DocumentRepository {



    override suspend fun loadDocumentsForUser(userId: String): List<Document> =
        documentSqlDao.loadDocumentsWithContentByUserId(OrderBy.NAME.type, userId)

    override suspend fun loadDocumentsForFolder(folderId: String): List<Document> =
        documentSqlDao.loadDocumentByParentId(folderId)

    override suspend fun loadFavDocumentsForUser(orderBy: String, userId: String): List<Document> =
        documentSqlDao.loadFavDocumentsWithContentByUserId(orderBy, userId)

    //    Here!!
    override fun listenForDocumentsByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<List<Document>> {
        TODO()
    }

    override suspend fun loadDocumentsForUserAfterTime(
        orderBy: String,
        userId: String,
        instant: Instant
    ): List<Document> {
        return documentSqlDao.loadDocumentsWithContentByUserIdAfterTime(
            userId,
            instant.toEpochMilliseconds()
        )
    }

    override suspend fun loadDocumentById(id: String): Document? =
        documentSqlDao.loadDocumentWithContentById(id)

    override suspend fun loadDocumentByIds(ids: List<String>): List<Document> =
        ids.mapNotNull { id ->
            loadDocumentById(id)
        }

    override suspend fun loadDocumentsWithContentByIds(
        ids: List<String>,
        orderBy: String
    ): List<Document> =
        documentSqlDao.loadDocumentWithContentByIds(ids)

    override suspend fun saveDocument(document: Document) {
        documentSqlDao.insertDocumentWithContent(document)
    }

    override suspend fun saveDocumentMetadata(document: Document) {
        documentSqlDao.insertDocument(document)
    }

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        documentSqlDao.insertStoryStep(storyStep, position.toLong(), documentId)
    }

    override suspend fun deleteDocument(document: Document) {
        documentSqlDao.deleteDocumentById(document.id)
    }

    override suspend fun deleteDocumentByIds(ids: Set<String>) {
        documentSqlDao.deleteDocumentByIds(ids)
    }

    override suspend fun deleteDocumentByFolder(folderId: String) {
        documentSqlDao.deleteDocumentsByFolderId(folderId)
    }

    override suspend fun deleteByUserId(userId: String) {
        documentSqlDao.deleteDocumentsByUserId(userId)
    }

    override suspend fun favoriteDocumentByIds(ids: Set<String>) {
        ids.forEach { id ->
            documentSqlDao.favoriteById(id)
        }
    }

    override suspend fun unFavoriteDocumentByIds(ids: Set<String>) {
        ids.forEach { id ->
            documentSqlDao.unFavoriteById(id)
        }
    }

    override suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        TODO("Not yet implemented")
    }
}

package io.writeopia.sdk.persistence.sqldelight.dao.sql

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.sdk.persistence.sqldelight.dao.DocumentSqlDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class SqlDelightDocumentRepository(
    private val documentSqlDao: DocumentSqlDao
) : DocumentRepository {

    private val _documentState = MutableStateFlow<Map<String, List<Document>>>(emptyMap())

    override suspend fun loadDocumentsForUser(userId: String): List<Document> =
        documentSqlDao.loadDocumentsWithContentByUserId(OrderBy.NAME.type, userId)

    override suspend fun loadDocumentsForFolder(folderId: String): List<Document> =
        documentSqlDao.loadDocumentByParentId(folderId)

    override suspend fun loadFavDocumentsForUser(orderBy: String, userId: String): List<Document> =
        documentSqlDao.loadFavDocumentsWithContentByUserId(orderBy, userId)

    override suspend fun loadDocumentsByParentId(parentId: String): List<Document> =
        documentSqlDao.loadDocumentByParentId(parentId)

    override fun listenForDocumentsByParentId(
        parentId: String,
        coroutineScope: CoroutineScope?
    ): Flow<Map<String, List<Document>>> {
        coroutineScope?.launch {
            SelectedIds.ids.add(parentId)
            refreshDocuments()
        }

        return _documentState
    }

    override suspend fun stopListeningForFoldersByParentId(parentId: String) {
        SelectedIds.ids.remove(parentId)
        refreshDocuments()
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

        refreshDocuments()
    }

    override suspend fun deleteDocumentByIds(ids: Set<String>) {
        documentSqlDao.deleteDocumentByIds(ids)

        refreshDocuments()
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

        refreshDocuments()
    }

    override suspend fun unFavoriteDocumentByIds(ids: Set<String>) {
        ids.forEach { id ->
            documentSqlDao.unFavoriteById(id)
        }

        refreshDocuments()
    }

    override suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun moveToFolder(documentId: String, parentId: String) {
        documentSqlDao.moveToFolder(documentId, parentId)
        refreshDocuments()
    }

    override suspend fun refreshDocuments() {
        _documentState.value = SelectedIds.ids.associateWith { id ->
            documentSqlDao.loadDocumentByParentId(id)
        }
    }
}

private object SelectedIds {
    val ids = mutableSetOf<String>()
}

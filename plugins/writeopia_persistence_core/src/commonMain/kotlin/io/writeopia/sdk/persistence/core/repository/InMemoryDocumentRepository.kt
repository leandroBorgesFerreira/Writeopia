package io.writeopia.sdk.persistence.core.repository

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Instant

class InMemoryDocumentRepository : DocumentRepository {

    private val documentsMap: MutableMap<String, Document> = mutableMapOf()

    override suspend fun loadDocumentsForUser(folderId: String): List<Document> =
        documentsMap.values.toList()

    override suspend fun loadDocumentsForFolder(folderId: String): List<Document> =
        documentsMap.values.toList()

    override suspend fun loadFavDocumentsForUser(orderBy: String, userId: String): List<Document> =
        documentsMap.values.filter { document -> document.favorite }

    override suspend fun loadDocumentsForUserAfterTime(
        orderBy: String,
        userId: String,
        instant: Instant
    ): List<Document> = documentsMap.values.toList()

    override suspend fun loadDocumentById(id: String): Document? = documentsMap[id]

    override suspend fun loadDocumentByIds(ids: List<String>): List<Document> =
        ids.mapNotNull { id ->
            documentsMap[id]
        }

    override fun listenForDocumentsByParentId(
        parentId: String,
        coroutineScope: CoroutineScope?
    ): Flow<Map<String, List<Document>>> =
        MutableStateFlow(emptyMap())

    override suspend fun loadDocumentsWithContentByIds(
        ids: List<String>,
        orderBy: String
    ): List<Document> {
        val idSet = ids.toSet()

        return documentsMap.filter { (key, _) -> idSet.contains(key) }.values.toList()
    }

    override suspend fun saveDocument(document: Document) {
        documentsMap[document.id] = document
    }

    override suspend fun saveDocumentMetadata(document: Document) {
        documentsMap[document.id]?.let { currentDocument ->
            documentsMap[document.id] = currentDocument.copy(title = document.title)
        }
    }

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        documentsMap[documentId]?.let { document ->
            val newContent = document.content + (position to storyStep)
            documentsMap[documentId] = document.copy(content = newContent)
        }
    }

    override suspend fun deleteDocument(document: Document) {
        documentsMap.remove(document.id)
    }

    override suspend fun deleteDocumentByIds(ids: Set<String>) {
        ids.forEach(documentsMap::remove)
    }

    override suspend fun deleteByUserId(userId: String) {
        documentsMap.clear()
    }

    override suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String) {
    }

    override suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
    }

    override suspend fun favoriteDocumentByIds(ids: Set<String>) {
        setFavorite(ids, true)
    }

    override suspend fun unFavoriteDocumentByIds(ids: Set<String>) {
        setFavorite(ids, false)
    }

    private fun setFavorite(ids: Set<String>, isFavorite: Boolean) {
        ids.forEach { id ->
            documentsMap[id]?.copy(favorite = isFavorite)?.let { document ->
                documentsMap[id] = document
            }
        }
    }

    override suspend fun deleteDocumentByFolder(folderId: String) {
    }

    override suspend fun moveToFolder(documentId: String, parentId: String) {
    }

    override suspend fun refreshDocuments() {
    }

    override suspend fun stopListeningForFoldersByParentId(parentId: String) {
    }

    override suspend fun loadDocumentsByParentId(parentId: String): List<Document> =
        emptyList()
}

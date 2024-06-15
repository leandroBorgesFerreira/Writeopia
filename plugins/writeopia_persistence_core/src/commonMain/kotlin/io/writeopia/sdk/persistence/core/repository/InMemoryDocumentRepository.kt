package io.writeopia.sdk.persistence.core.repository

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.persistence.core.extensions.sortWithOrderBy
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import kotlinx.datetime.Instant

class InMemoryDocumentRepository : DocumentRepository {

    private val documentsMap: MutableMap<String, Document> = mutableMapOf()

    override suspend fun loadDocumentsForUser(orderBy: String, userId: String): List<Document> =
        documentsMap.values.toList().sortWithOrderBy(OrderBy.fromString(orderBy))

    override suspend fun loadFavDocumentsForUser(orderBy: String, userId: String): List<Document> =
        loadDocumentsForUser(orderBy, userId).filter { document -> document.favorite }

    override suspend fun loadDocumentsForUserAfterTime(
        orderBy: String,
        userId: String,
        instant: Instant
    ): List<Document> = documentsMap.values.toList()

    override suspend fun loadDocumentById(id: String): Document? = documentsMap.values.firstOrNull()

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
        ids.forEach { id ->
            documentsMap[id]?.copy(favorite = true)?.let { document ->
                documentsMap[id] = document
            }
        }
    }
}

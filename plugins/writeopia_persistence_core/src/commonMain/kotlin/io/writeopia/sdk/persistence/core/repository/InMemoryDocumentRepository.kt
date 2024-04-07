package io.writeopia.sdk.persistence.core.repository

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep

class InMemoryDocumentRepository : DocumentRepository {

    private val documentsMap: MutableMap<String, Document> = mutableMapOf()

    override suspend fun loadDocumentsForUser(orderBy: String, userId: String): List<Document> =
        documentsMap.values.toList().also {
            println("loadDocumentsForUser")
        }

    override suspend fun loadDocumentById(id: String): Document? = documentsMap.values.firstOrNull()

    override suspend fun loadDocumentsWithContentByIds(
        ids: List<String>,
        orderBy: String
    ): List<Document> {
        val idSet = ids.toSet()

        return documentsMap.filter { (key, _) -> idSet.contains(key) }.values.toList()
    }

    override suspend fun saveDocument(document: Document) {
        println("saveDocument")
        documentsMap[document.id] = document

        println("documents state: ${documentsMap.values.joinToString { it.title }}")
    }

    override suspend fun saveDocumentMetadata(document: Document) {
        documentsMap[document.id]?.let { currentDocument ->
            documentsMap[document.id] = currentDocument.copy(title = document.title)
        }
    }

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        println("saveStoryStep")
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
}
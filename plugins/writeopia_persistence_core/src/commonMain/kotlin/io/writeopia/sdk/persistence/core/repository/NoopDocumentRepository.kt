package io.writeopia.sdk.persistence.core.repository

import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep

class NoopDocumentRepository : DocumentRepository {
    override suspend fun loadDocumentsForUser(orderBy: String, userId: String): List<Document> {
        TODO("Not yet implemented")
    }

    override suspend fun loadDocumentById(id: String): Document? = null

    override suspend fun loadDocumentsWithContentByIds(
        ids: List<String>,
        orderBy: String
    ): List<Document> = emptyList()

    override suspend fun saveDocument(document: Document) {
    }

    override suspend fun saveDocumentMetadata(document: Document) {
    }

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
    }

    override suspend fun deleteDocument(document: Document) {
    }

    override suspend fun deleteDocumentByIds(ids: Set<String>) {
    }

    override suspend fun deleteByUserId(userId: String) {
    }

    override suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String) {
    }

    override suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
    }
}
package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

/**
 * DocumentRepository is the repository for using simple CRUD operations in [Document].
 * The implementations of this interface shouldn't control order (sorting) or oder configurations,
 * those need to be passed as parameters.
 */
interface DocumentRepository : DocumentUpdate {

    suspend fun loadDocuments(orderBy: String): List<Document>

    suspend fun loadDocumentById(id: String): Document?

    suspend fun loadDocumentsById(ids: List<String>, orderBy: String): List<Document>

    /**
     * Saves document. Both with content and meta data.
     */
    override suspend fun saveDocument(document: Document)

    /**
     * Saves the document meta data. Use this was updating the content of the document is not
     * necessary. This is a much lighter operation than [saveDocument], because it is not
     * necessary to save/update all lines of content.
     */
    override suspend fun saveDocumentMetadata(document: Document)

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String)

    suspend fun deleteDocument(document: Document)

    suspend fun deleteDocumentById(ids: Set<String>)

    /**
     * Deleted all the documents of a User
     */
    suspend fun deleteByUserId(userId: String)
}

package io.writeopia.sdk.persistence.core.repository

import io.writeopia.sdk.manager.DocumentUpdate
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import kotlinx.datetime.Instant

/**
 * DocumentRepository is the repository for using simple CRUD operations in [Document].
 * The implementations of this interface shouldn't control order (sorting) or oder configurations,
 * those need to be passed as parameters.
 */
interface DocumentRepository : DocumentUpdate {

    suspend fun loadDocumentsForFolder(folderId: String): List<Document>

    suspend fun loadFavDocumentsForUser(userId: String): List<Document>

    suspend fun loadDocumentsForUserAfterTime(
        orderBy: String,
        userId: String,
        instant: Instant
    ): List<Document>

    suspend fun loadDocumentById(id: String): Document?

    suspend fun loadDocumentByIds(ids: List<String>): List<Document>

    suspend fun loadDocumentsWithContentByIds(ids: List<String>, orderBy: String): List<Document>

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

    suspend fun deleteDocumentByIds(ids: Set<String>)

    suspend fun favoriteDocumentByIds(ids: Set<String>)

    suspend fun unFavoriteDocumentByIds(ids: Set<String>)

    /**
     * Deleted all the documents of a User
     */
    suspend fun deleteByUserId(userId: String)

    /**
     * Moves all tickets from one user to another. Use this we would like to pass all the data of
     * documents to another user. When the offline user becomes a new online user, all documents
     * should be moved to the new online user.
     */
    suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String)
}

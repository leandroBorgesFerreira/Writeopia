package io.writeopia.sdk.persistence.dao.room

import io.writeopia.sdk.model.document.DocumentInfo
import io.writeopia.sdk.model.document.info
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.search.DocumentSearch
import io.writeopia.sdk.repository.DocumentRepository
import io.writeopia.sdk.persistence.dao.DocumentEntityDao
import io.writeopia.sdk.persistence.dao.StoryUnitEntityDao
import io.writeopia.sdk.persistence.entity.document.DocumentEntity
import io.writeopia.sdk.persistence.entity.story.StoryStepEntity
import io.writeopia.sdk.persistence.parse.toEntity
import io.writeopia.sdk.persistence.parse.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class RoomDocumentRepository(
    private val documentEntityDao: DocumentEntityDao,
    private val storyUnitEntityDao: StoryUnitEntityDao? = null
) : DocumentRepository, DocumentSearch {

    private val documentsState: MutableStateFlow<Map<String, List<Document>>> =
        MutableStateFlow(emptyMap())

    override suspend fun loadDocumentsForFolder(folderId: String): List<Document> =
        documentEntityDao.loadDocumentsByParentId(folderId).map { it.toModel() }

    override suspend fun loadFavDocumentsForUser(orderBy: String, userId: String): List<Document> =
        emptyList()

    override suspend fun deleteDocumentByFolder(folderId: String) {
    }

    override suspend fun search(query: String): List<Document> =
        documentEntityDao.search(query).map { it.toModel() }

    override suspend fun getLastUpdatedAt(): List<Document> =
        documentEntityDao.selectByLastUpdated().map { it.toModel() }

    override suspend fun listenForDocumentsByParentId(
        parentId: String
    ): Flow<Map<String, List<Document>>> =
        documentEntityDao.listenForDocumentsWithContentByParentId(parentId)
            .map { resultsMap ->
                resultsMap.map { (documentEntity, storyEntity) ->
                    val content = loadInnerSteps(storyEntity)
                    documentEntity.toModel(content)
                }.groupBy { it.parentId }
            }

    override suspend fun listenForDocumentInfoById(id: String): Flow<DocumentInfo?> =
        documentEntityDao.listenForDocumentById(id).map { entity ->
            entity?.toModel()?.info()
        }

    override suspend fun loadDocumentsForUser(userId: String): List<Document> =
        documentEntityDao.loadDocumentsWithContentForUser(userId)
            .map { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            }

    override suspend fun loadDocumentsForUserAfterTime(
        orderBy: String,
        userId: String,
        instant: Instant
    ): List<Document> {
        throw IllegalStateException("This method is not supported")
    }

    override suspend fun favoriteDocumentByIds(ids: Set<String>) {
        setFavorite(ids, true)
    }

    override suspend fun unFavoriteDocumentByIds(ids: Set<String>) {
        setFavorite(ids, false)
    }

    override suspend fun loadDocumentById(id: String): Document? =
        documentEntityDao.loadDocumentById(id)?.let { documentEntity ->
            val content = loadInnerSteps(
                storyUnitEntityDao?.loadDocumentContent(documentEntity.id) ?: emptyList()
            )
            documentEntity.toModel(content)
        }

    override suspend fun loadDocumentByIds(ids: List<String>): List<Document> =
        documentEntityDao.loadDocumentByIds(ids).map { documentEntity ->
            val content = loadInnerSteps(
                storyUnitEntityDao?.loadDocumentContent(documentEntity.id) ?: emptyList()
            )
            documentEntity.toModel(content)
        }

    override suspend fun loadDocumentsWithContentByIds(
        ids: List<String>,
        orderBy: String
    ): List<Document> =
        documentEntityDao.loadDocumentWithContentByIds(ids, orderBy)
            .entries
            .map { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            }

    override suspend fun saveDocument(document: Document) {
        saveDocumentMetadata(document)

        document.content.toEntity(document.id).let { data ->
            storyUnitEntityDao?.deleteDocumentContent(documentId = document.id)
            storyUnitEntityDao?.insertStoryUnits(*data.toTypedArray())
        }
    }

    override suspend fun saveDocumentMetadata(document: Document) {
        documentEntityDao.insertDocuments(document.toEntity())
    }

    override suspend fun deleteDocument(document: Document) {
        documentEntityDao.deleteDocuments(document.toEntity())
    }

    override suspend fun deleteDocumentByIds(ids: Set<String>) {
        // The user ID is not relevant in the way to delete documents
        documentEntityDao.deleteDocuments(
            *ids.map {
                DocumentEntity.createById(
                    it,
                    "",
                    parentId = ""
                )
            }.toTypedArray()
        )
    }

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        println("saving story steps: ${storyStep.spans.joinToString { it.toText() }}")
        storyUnitEntityDao?.insertStoryUnits(storyStep.toEntity(position, documentId))
    }

    override suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        storyUnitEntityDao?.updateStoryStep(storyStep.toEntity(position, documentId))
    }

    override suspend fun deleteByUserId(userId: String) {
        documentEntityDao.deleteDocumentsByUserId(userId)
    }

    override suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String) {
        documentEntityDao.moveDocumentsToNewUser(oldUserId, newUserId)
    }

    override suspend fun moveToFolder(documentId: String, parentId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun loadDocumentsByParentId(parentId: String): List<Document> =
        documentEntityDao.loadDocumentsByParentId(parentId).map { it.toModel() }

    /**
     * This method removes the story units that are not in the root level (they don't have parents)
     * and loads the inner steps of the steps that have children.
     */
    private suspend fun loadInnerSteps(storyEntities: List<StoryStepEntity>): Map<Int, StoryStep> =
        storyEntities.filter { entity -> entity.parentId == null }
            .associateBy { entity -> entity.position }
            .mapValues { (_, entity) ->
                if (entity.hasInnerSteps) {
                    val innerSteps = storyUnitEntityDao?.queryInnerSteps(entity.id) ?: emptyList()

                    entity.toModel(innerSteps)
                } else {
                    entity.toModel()
                }
            }

    private suspend fun setFavorite(ids: Set<String>, isFavorite: Boolean) {
        ids.mapNotNull { id ->
            loadDocumentById(id)
        }.forEach { document ->
            documentEntityDao.updateDocument(document.copy(favorite = isFavorite).toEntity())
        }
    }

    override suspend fun refreshDocuments() {
    }

    override suspend fun stopListeningForFoldersByParentId(parentId: String) {
    }

    override suspend fun loadOutdatedDocuments(folderId: String): List<Document> {
        TODO("Not yet implemented")
    }
}

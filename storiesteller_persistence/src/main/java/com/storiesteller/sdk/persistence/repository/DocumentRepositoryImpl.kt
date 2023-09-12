package com.storiesteller.sdk.persistence.repository

import com.storiesteller.sdk.manager.DocumentRepository
import com.storiesteller.sdk.models.document.Document
import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.persistence.dao.DocumentDao
import com.storiesteller.sdk.persistence.dao.StoryUnitDao
import com.storiesteller.sdk.persistence.entity.document.DocumentEntity
import com.storiesteller.sdk.persistence.entity.story.StoryStepEntity
import com.storiesteller.sdk.persistence.parse.toEntity
import com.storiesteller.sdk.persistence.parse.toModel

class DocumentRepositoryImpl(
    private val documentDao: DocumentDao,
    private val storyUnitDao: StoryUnitDao
) : DocumentRepository {

    override suspend fun loadDocumentsForUser(orderBy: String, userId: String): List<Document> =
        documentDao.loadDocumentsWithContentForUser(orderBy, userId)
            ?.map { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            } ?: emptyList()

    override suspend fun loadDocumentById(id: String): Document? {
        val documentEntity = documentDao.loadDocumentById(id)
        val content = loadInnerSteps(storyUnitDao.loadDocumentContent(id))
        return documentEntity?.toModel(content)
    }

    override suspend fun loadDocumentsById(ids: List<String>, orderBy: String): List<Document> =
        documentDao.loadDocumentWithContentByIds(ids, orderBy)
            .entries
            .map { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            }

    override suspend fun saveDocument(document: Document) {
        saveDocumentMetadata(document)

        document.content.toEntity(document.id).let { data ->
            storyUnitDao.deleteDocumentContent(documentId = document.id)
            storyUnitDao.insertStoryUnits(*data.toTypedArray())
        }
    }

    override suspend fun saveDocumentMetadata(document: Document) {
        documentDao.insertDocuments(document.toEntity())
    }

    override suspend fun deleteDocument(document: Document) {
        documentDao.deleteDocuments(document.toEntity())
    }

    override suspend fun deleteDocumentById(ids: Set<String>) {
        // The user ID is not relevant in the way to delete documents
        documentDao.deleteDocuments(*ids.map { DocumentEntity.createById(it, "") }.toTypedArray())
    }

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        storyUnitDao.insertStoryUnits(storyStep.toEntity(position, documentId))
    }

    override suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        storyUnitDao.updateStoryStep(storyStep.toEntity(position, documentId))
    }

    /**
     * This method removes the story units that are not in the root level (they don't have parents)
     * and loads the inner steps of the steps that have children.
     */
    private suspend fun loadInnerSteps(storyEntities: List<StoryStepEntity>): Map<Int, StoryStep> =
        storyEntities.filter { entity -> entity.parentId == null }
            .associateBy { entity -> entity.position }
            .mapValues { (_, entity) ->
                if (entity.hasInnerSteps) {
                    val innerSteps = storyUnitDao.queryInnerSteps(entity.id)

                    entity.toModel(innerSteps)
                } else {
                    entity.toModel()
                }
            }

    override suspend fun deleteByUserId(userId: String) {
        documentDao.deleteDocumentsByUserId(userId)
    }

    override suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String) {
        documentDao.moveDocumentsToNewUser(oldUserId, newUserId)
    }
}

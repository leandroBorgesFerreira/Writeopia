package com.github.leandroborgesferreira.storyteller.persistence.repository

import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroborgesferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.StoryStepEntity
import com.github.leandroborgesferreira.storyteller.persistence.parse.toEntity
import com.github.leandroborgesferreira.storyteller.persistence.parse.toModel

class DocumentRepositoryImpl(
    private val documentDao: DocumentDao,
    private val storyUnitDao: StoryUnitDao
) : DocumentRepository {

    override suspend fun loadDocuments(orderBy: String): List<Document> =
        documentDao.loadDocumentWithContent(orderBy)
            ?.map { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            } ?: emptyList()

    override suspend fun loadDocumentById(id: String): Document? {
        val documentEntity = documentDao.loadDocumentById(id)
        val content = loadInnerSteps(storyUnitDao.loadDocumentContent(id))
        return documentEntity.toModel(content)
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

        document.content?.toEntity(document.id)?.let { data ->
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
}

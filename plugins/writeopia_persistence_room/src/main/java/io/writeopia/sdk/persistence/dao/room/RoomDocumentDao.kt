package io.writeopia.sdk.persistence.dao.room

import io.writeopia.sdk.persistence.core.dao.DocumentDao
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.persistence.dao.DocumentEntityDao
import io.writeopia.sdk.persistence.dao.StoryUnitEntityDao
import io.writeopia.sdk.persistence.entity.document.DocumentEntity
import io.writeopia.sdk.persistence.entity.story.StoryStepEntity
import io.writeopia.sdk.persistence.parse.toEntity
import io.writeopia.sdk.persistence.parse.toModel

class RoomDocumentDao(
    private val documentEntityDao: DocumentEntityDao,
    private val storyUnitEntityDao: StoryUnitEntityDao
) : DocumentDao {

    override suspend fun loadDocumentsForUser(orderBy: String, userId: String): List<Document> =
        documentEntityDao.loadDocumentsWithContentForUser(orderBy, userId)
            ?.map { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            } ?: emptyList()

    override suspend fun loadDocumentById(id: String): Document? {
        val documentEntity = documentEntityDao.loadDocumentById(id)
        val content = loadInnerSteps(storyUnitEntityDao.loadDocumentContent(id))
        return documentEntity?.toModel(content)
    }

    override suspend fun loadDocumentsWithContentByIds(ids: List<String>, orderBy: String): List<Document> =
        documentEntityDao.loadDocumentWithContentByIds(ids, orderBy)
            .entries
            .map { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            }

    override suspend fun saveDocument(document: Document) {
        saveDocumentMetadata(document)

        document.content.toEntity(document.id).let { data ->
            storyUnitEntityDao.deleteDocumentContent(documentId = document.id)
            storyUnitEntityDao.insertStoryUnits(*data.toTypedArray())
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
        documentEntityDao.deleteDocuments(*ids.map { DocumentEntity.createById(it, "") }.toTypedArray())
    }

    override suspend fun saveStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        storyUnitEntityDao.insertStoryUnits(storyStep.toEntity(position, documentId))
    }

    override suspend fun updateStoryStep(storyStep: StoryStep, position: Int, documentId: String) {
        storyUnitEntityDao.updateStoryStep(storyStep.toEntity(position, documentId))
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
                    val innerSteps = storyUnitEntityDao.queryInnerSteps(entity.id)

                    entity.toModel(innerSteps)
                } else {
                    entity.toModel()
                }
            }

    override suspend fun deleteByUserId(userId: String) {
        documentEntityDao.deleteDocumentsByUserId(userId)
    }

    override suspend fun moveDocumentsToNewUser(oldUserId: String, newUserId: String) {
        documentEntityDao.moveDocumentsToNewUser(oldUserId, newUserId)
    }
}

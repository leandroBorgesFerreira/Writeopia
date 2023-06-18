package com.github.leandroborgesferreira.storyteller.persistence.repository

import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.persistence.parse.toEntity
import com.github.leandroborgesferreira.storyteller.persistence.parse.toModel
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroborgesferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity
import com.github.leandroborgesferreira.storyteller.persistence.entity.story.StoryUnitEntity

/**
 * Evaluate to move this class to persistence module
 */
class DocumentRepository(
    private val documentDao: DocumentDao,
    private val storyUnitDao: StoryUnitDao
) : DocumentRepository {

    suspend fun loadDocuments(): List<DocumentEntity> = documentDao.loadAllDocuments()

    suspend fun loadDocumentBy(id: String): Document? =
        documentDao.loadDocumentWithContentById(id)
            ?.entries
            ?.firstOrNull()
            ?.let { (documentEntity, storyEntity) ->
                val content = loadInnerSteps(storyEntity)
                documentEntity.toModel(content)
            }

    suspend fun saveDocument(document: Document) {
        documentDao.insertDocuments(document.toEntity())

        document.content?.toEntity(document.id)?.let { data ->
            storyUnitDao.deleteDocumentContent(documentId = document.id)
            storyUnitDao.insertStoryUnits(*data.toTypedArray())
        }
    }

    override suspend fun save(documentId: String, content: Map<Int, StoryStep>) {
        storyUnitDao.deleteDocumentContent(documentId = documentId)
        storyUnitDao.insertStoryUnits(*content.toEntity(documentId).toTypedArray())
    }

    /**
     * This method removes the story units that are not in the root level (they don't have parents)
     * and loads the inner steps of the steps that have children.
     */
    private suspend fun loadInnerSteps(storyEntities: List<StoryUnitEntity>): Map<Int, StoryStep> =
        storyEntities.filter { entity -> entity.parentId == null }
            .sortedBy { entity -> entity.position } //Todo: Move this to the SQL query
            .associateBy { entity -> entity.position }
            .mapValues { (_, entity) ->
                if (entity.hasInnerSteps) {
                    val innerSteps = storyUnitDao.queryInnerSteps(entity.id)

                    entity.toModel(innerSteps)
                } else {
                    entity.toModel()
                }
            }
}

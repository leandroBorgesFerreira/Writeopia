package com.github.leandroborgesferreira.storyteller.persistence.repository

import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.persistence.parse.toEntity
import com.github.leandroborgesferreira.storyteller.persistence.parse.toModel
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroborgesferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity

/**
 * Evaluate to move this class to persistence module
 */
class DocumentRepository(
    private val documentDao: DocumentDao,
    private val storyUnitDao: StoryUnitDao
) : DocumentRepository {

    suspend fun loadDocuments(): List<DocumentEntity> = documentDao.loadAllDocuments()

    suspend fun loadDocumentBy(id: String): Document? {
        return documentDao.loadDocumentWithContent(id)?.map { (documentEntity, storyEntity) ->
            val content = storyEntity
                .filter { entity -> entity.parentId == null }
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

            documentEntity.toModel(content)
        }?.firstOrNull()
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
}

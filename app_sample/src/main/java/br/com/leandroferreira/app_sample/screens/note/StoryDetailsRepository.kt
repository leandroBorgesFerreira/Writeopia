package br.com.leandroferreira.app_sample.screens.note

import br.com.leandroferreira.app_sample.parse.toEntity
import br.com.leandroferreira.app_sample.parse.toModel
import br.com.leandroferreira.storyteller.model.document.Document
import br.com.leandroferreira.storyteller.persistence.dao.DocumentDao
import br.com.leandroferreira.storyteller.persistence.dao.StoryUnitDao

class StoryDetailsRepository(
    private val documentDao: DocumentDao,
    private val storyUnitDao: StoryUnitDao
) {

    suspend fun loadDocumentBy(id: String): Document? {
        return documentDao.loadDocumentWithContent(id)?.map { (documentEntity, storyEntity) ->
            val content = storyEntity
                .sortedBy { story -> story.position } //Todo: Move this to the SQL query
                .associateBy { story -> story.position }
                .mapValues { (_, story) -> story.toModel() }

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

}

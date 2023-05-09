package br.com.leandroferreira.app_sample.screens.note

import br.com.leandroferreira.app_sample.parse.toModel
import br.com.leandroferreira.storyteller.model.document.Document
import br.com.leandroferreira.storyteller.persistence.dao.DocumentDao

class StoryDetailsRepository(private val documentDao: DocumentDao) {

    suspend fun loadDocumentBy(id: String): Document? {
        return documentDao.loadDocumentWithContent(id)?.map { (documentEntity, storyEntity) ->
            val content = storyEntity
                .sortedBy { storyEntity -> storyEntity.position } //Todo: Move this to the SQL query
                .associateBy { storyEntity -> storyEntity.position }
                .mapValues { (_, storyEntity) -> storyEntity.toModel() }

            documentEntity.toModel(content)
        }?.firstOrNull()
    }

}

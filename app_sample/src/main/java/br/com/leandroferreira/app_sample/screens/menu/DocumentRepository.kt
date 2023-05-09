package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import br.com.leandroferreira.app_sample.data.syncHistory
import br.com.leandroferreira.app_sample.parse.toEntity
import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.persistence.dao.DocumentDao
import br.com.leandroferreira.storyteller.persistence.dao.StoryUnitDao
import br.com.leandroferreira.storyteller.persistence.entity.document.DocumentEntity
import java.util.UUID

class DocumentRepository(
    private val documentDao: DocumentDao,
    private val storyUnitDao: StoryUnitDao,
) {

    suspend fun loadDocuments(): List<DocumentEntity> = documentDao.loadAllDocuments()

    suspend fun mockData(context: Context) {
        val documentId = UUID.randomUUID().toString()

        documentDao.insertDocuments(
            DocumentEntity(documentId, "My notes 1")
        )

        val entities = syncHistory(context).map { (position, storyUnit) ->
            if (storyUnit is GroupStep) {
                storyUnit.toEntity(position, documentId)
            } else {
                (storyUnit as StoryStep).toEntity(position, documentId)
            }
        }

        storyUnitDao.insertDocuments(*entities.toTypedArray())
    }
}

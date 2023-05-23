package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import br.com.leandroferreira.app_sample.data.supermarketList
import br.com.leandroferreira.app_sample.data.syncHistory
import com.github.leandroferreira.storyteller.persistence.parse.toEntity
import com.github.leandroferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroferreira.storyteller.persistence.entity.document.DocumentEntity
import java.util.UUID

class NotesRepository(
    private val documentDao: DocumentDao,
    private val storyUnitDao: StoryUnitDao,
) {

    suspend fun loadDocuments(): List<DocumentEntity> = documentDao.loadAllDocuments()

    suspend fun mockData(context: Context) {
        val travelNoteId = UUID.randomUUID().toString()
        val superMarketNoteId = UUID.randomUUID().toString()

        documentDao.insertDocuments(
            DocumentEntity(travelNoteId, "Travel Note"),
            DocumentEntity(superMarketNoteId, "Supermarket List")
        )

        val travelContent = syncHistory(context).toEntity(travelNoteId)
        val supermarketContent = supermarketList().toEntity(superMarketNoteId)

        storyUnitDao.insertStoryUnits(*travelContent.toTypedArray())
        storyUnitDao.insertStoryUnits(*supermarketContent.toTypedArray())
    }
}


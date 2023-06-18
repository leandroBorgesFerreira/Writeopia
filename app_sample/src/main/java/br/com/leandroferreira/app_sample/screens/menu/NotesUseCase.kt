package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import br.com.leandroferreira.app_sample.data.supermarketList
import br.com.leandroferreira.app_sample.data.mockHistory
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao
import com.github.leandroborgesferreira.storyteller.persistence.dao.StoryUnitDao
import com.github.leandroborgesferreira.storyteller.persistence.entity.document.DocumentEntity
import com.github.leandroborgesferreira.storyteller.persistence.parse.toEntity
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepository
import java.util.UUID

class NotesUseCase(
    private val documentDao: DocumentDao, //Todo: Remove
    private val storyUnitDao: StoryUnitDao, //Todo: Remove
    private val documentRepository: DocumentRepository
) {

    suspend fun loadDocuments(): List<Document> = documentRepository.loadDocuments()

    suspend fun mockData(context: Context) {
        val travelNoteId = UUID.randomUUID().toString()
        val superMarketNoteId = UUID.randomUUID().toString()

        documentDao.insertDocuments(
            DocumentEntity(travelNoteId, "Travel Note"),
            DocumentEntity(superMarketNoteId, "Supermarket List")
        )

        val travelContent = mockHistory(context).toEntity(travelNoteId)
        val supermarketContent = supermarketList().toEntity(superMarketNoteId)

        storyUnitDao.insertStoryUnits(*travelContent.toTypedArray())
        storyUnitDao.insertStoryUnits(*supermarketContent.toTypedArray())
    }
}


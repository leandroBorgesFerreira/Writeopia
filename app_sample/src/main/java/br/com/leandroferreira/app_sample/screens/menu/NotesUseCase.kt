package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import br.com.leandroferreira.app_sample.data.supermarketList
import br.com.leandroferreira.app_sample.data.travelHistory
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepository
import java.util.UUID

class NotesUseCase(private val documentRepository: DocumentRepository) {

    suspend fun loadDocuments(): List<Document> = documentRepository.loadDocuments()

    suspend fun mockData(context: Context) {
        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Travel Note",
                content = travelHistory(context),
            )
        )

        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Supermarket List",
                content = supermarketList(),
            )
        )
    }
}

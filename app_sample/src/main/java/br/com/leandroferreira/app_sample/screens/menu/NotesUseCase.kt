package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import br.com.leandroferreira.app_sample.data.supermarketList
import br.com.leandroferreira.app_sample.data.travelHistory
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl
import java.util.Date
import java.util.UUID

class NotesUseCase(private val documentRepository: DocumentRepositoryImpl) {

    suspend fun loadDocuments(): List<Document> = documentRepository.loadDocuments()

    suspend fun mockData(context: Context) {
        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Travel Note",
                content = travelHistory(context),
                createdAt = Date(),
                lastUpdatedAt = Date()
            )
        )

        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Supermarket List",
                content = supermarketList(),
                createdAt = Date(),
                lastUpdatedAt = Date()
            )
        )
    }
}

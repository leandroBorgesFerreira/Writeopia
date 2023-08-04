package com.github.leandroborgesferreira.storytellerapp.note_menu.data.usecase

import android.content.Context
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.supermarketList
import com.github.leandroborgesferreira.storytellerapp.note_menu.data.travelHistory
import com.github.leandroborgesferreira.storyteller.manager.DocumentRepository
import com.github.leandroborgesferreira.storyteller.model.document.Document
import java.util.Date
import java.util.UUID

/**
 * UseCase responsible to perform CRUD operations in the Notes (Documents) of the app taking in to
 * consideration the configuration desired in the app.
 */
class NotesUseCase(
    private val documentRepository: DocumentRepository,
    private val notesConfig: NotesConfigurationRepository
) {

    suspend fun loadDocuments(): List<Document> =
        notesConfig.getOrderPreference()
            ?.let { orderBy -> documentRepository.loadDocuments(orderBy) }!!

    suspend fun duplicateDocuments(ids: List<String>) {
        notesConfig.getOrderPreference()?.let { orderBy ->
            documentRepository.loadDocumentsById(ids, orderBy)
        }?.let { documents ->
            documents.map { document ->
                document.copy(
                    id = UUID.randomUUID().toString(),
                    content = document.content?.mapValues { (_, storyStep) ->
                        storyStep.copy(id = UUID.randomUUID().toString())
                    })
            }
        }?.let { newDocuments ->
            newDocuments.forEach { document ->
                documentRepository.saveDocument(document)
            }
        }
    }

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

    suspend fun deleteNotes(ids: Set<String>) {
        documentRepository.deleteDocumentById(ids)
    }
}

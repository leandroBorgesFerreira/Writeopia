package io.storiesteller.note_menu.data.usecase

import io.storiesteller.note_menu.data.supermarketList
import io.storiesteller.note_menu.data.travelHistory
import io.storiesteller.sdk.manager.DocumentRepository
import io.storiesteller.sdk.models.document.Document
import io.storiesteller.utils_module.DISCONNECTED_USER_ID
import java.time.Instant
import java.util.UUID

/**
 * UseCase responsible to perform CRUD operations in the Notes (Documents) of the app taking in to
 * consideration the configuration desired in the app.
 */
internal class NotesUseCase(
    private val documentRepository: DocumentRepository,
    private val notesConfig: NotesConfigurationRepository
) {

    suspend fun loadDocumentsForUser(userId: String): List<Document> =
        notesConfig.getOrderPreference()
            ?.let { orderBy -> documentRepository.loadDocumentsForUser(orderBy, userId) }!!

    suspend fun duplicateDocuments(ids: List<String>) {
        notesConfig.getOrderPreference()?.let { orderBy ->
            documentRepository.loadDocumentsById(ids, orderBy)
        }?.let { documents ->
            documents.map { document ->
                document.copy(
                    id = UUID.randomUUID().toString(),
                    content = document.content.mapValues { (_, storyStep) ->
                        storyStep.copy(id = UUID.randomUUID().toString())
                    })
            }
        }?.let { newDocuments ->
            newDocuments.forEach { document ->
                documentRepository.saveDocument(document)
            }
        }
    }

    suspend fun mockData() {
        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Travel Note",
                content = travelHistory(),
                createdAt = Instant.now(),
                lastUpdatedAt = Instant.now(),
                userId = DISCONNECTED_USER_ID
            )
        )

        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Supermarket List",
                content = supermarketList(),
                createdAt = Instant.now(),
                lastUpdatedAt = Instant.now(),
                userId = DISCONNECTED_USER_ID
            )
        )
    }

    suspend fun deleteNotes(ids: Set<String>) {
        documentRepository.deleteDocumentById(ids)
    }
}

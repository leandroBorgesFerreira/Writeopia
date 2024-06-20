package io.writeopia.note_menu.data.usecase

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.models.id.GenerateId
import kotlinx.datetime.Instant

/**
 * UseCase responsible to perform CRUD operations in the Notes (Documents) of the app taking in to
 * consideration the configuration desired in the app.
 */
internal class NotesUseCase(
    private val documentRepository: DocumentRepository,
    private val notesConfig: ConfigurationRepository,
    private val folderRepository: FolderRepository
) {

    suspend fun loadRootContent(userId: String): List<MenuItem> {
        loadRootFoldersForUser(userId)

        return emptyList()
    }

    private suspend fun loadRootFoldersForUser(userId: String): List<Folder> =
        folderRepository.getRootFolders(userId)

    suspend fun loadDocumentsForFolder(userId: String): List<Document> {
        return documentRepository.loadDocumentsForFolder(userId)
    }

    suspend fun loadDocumentsForUser(userId: String): List<Document> =
        documentRepository.loadDocumentsForUser(userId)

    suspend fun loadFavDocumentsForUser(userId: String): List<Document> {
        return documentRepository.loadFavDocumentsForUser(userId)
    }

    suspend fun loadDocumentsForUserAfterTime(userId: String, time: Instant): List<Document> =
        notesConfig.getOrderPreference(userId)
            .let { orderBy ->
                documentRepository.loadDocumentsForUserAfterTime(
                    orderBy,
                    userId,
                    time
                )
            }

    suspend fun duplicateDocuments(ids: List<String>, userId: String) {
        notesConfig.getOrderPreference(userId).let { orderBy ->
            documentRepository.loadDocumentsWithContentByIds(ids, orderBy)
        }.map { document ->
            document.copy(
                id = GenerateId.generate(),
                content = document.content.mapValues { (_, storyStep) ->
                    storyStep.copy(id = GenerateId.generate())
                })
        }.forEach { document ->
            documentRepository.saveDocument(document)
        }
    }

    suspend fun saveDocument(document: Document) {
        documentRepository.saveDocument(document)
    }

    suspend fun deleteNotes(ids: Set<String>) {
        documentRepository.deleteDocumentByIds(ids)
    }

    suspend fun favoriteNotes(ids: Set<String>) {
        documentRepository.favoriteDocumentByIds(ids)
    }

    suspend fun unFavoriteNotes(ids: Set<String>) {
        documentRepository.unFavoriteDocumentByIds(ids)
    }
}

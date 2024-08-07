package io.writeopia.note_menu.data.usecase

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.data.repository.ConfigurationRepository
import io.writeopia.note_menu.data.repository.FolderRepository
import io.writeopia.note_menu.ui.dto.MenuItemUi
import io.writeopia.note_menu.utils.sortedWithOrderBy
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.document.MenuItem
import io.writeopia.sdk.models.id.GenerateId
import io.writeopia.sdk.persistence.core.repository.DocumentRepository
import io.writeopia.sdk.persistence.core.sorting.OrderBy
import io.writeopia.utils_module.collections.merge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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

    suspend fun createFolder(name: String, userId: String) {
        folderRepository.createFolder(Folder.fromName(name, userId))
    }

    suspend fun updateFolder(folder: Folder) {
        folderRepository.updateFolder(folder)
    }

    suspend fun moveItem(menuItem: MenuItemUi, parentId: String) {
        when (menuItem) {
            is MenuItemUi.DocumentUi -> {
                documentRepository.moveToFolder(menuItem.documentId, parentId)
            }

            is MenuItemUi.FolderUi -> {
                folderRepository.moveToFolder(menuItem.documentId, parentId)
            }
        }

        folderRepository.refreshFolders()
    }

    suspend fun loadDocumentsForUser(userId: String): List<Document> =
        documentRepository.loadDocumentsForUser(userId)

    suspend fun loadDocumentsForUserAfterTime(userId: String, time: Instant): List<Document> =
        notesConfig.getOrderPreference(userId)
            .let { orderBy ->
                documentRepository.loadDocumentsForUserAfterTime(
                    orderBy,
                    userId,
                    time
                )
            }

    /**
     * Listen and gets [MenuItem] groups by  parent folder.
     * This will return all folders that this method was called for (using the parentId).
     *
     * @param parentId The id of the folder
     * @param coroutineScope [CoroutineScope]
     */
    fun listenForMenuItemsByParentId(
        parentId: String,
        userIdFn: suspend () -> String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<MenuItem>>> =
        combine(
            listenForFoldersByParentId(parentId, coroutineScope),
            listenForDocumentsByParentId(parentId, coroutineScope),
            notesConfig.listenOrderPreference(userIdFn, coroutineScope)
        ) { folders, documents, orderPreference ->
            val order = orderPreference.takeIf { it.isNotEmpty() }?.let(OrderBy::fromString)
                ?: OrderBy.CREATE

            folders.merge(documents).mapValues { (_, menuItems) ->
                menuItems.sortedWithOrderBy(order)
            }
        }

    suspend fun stopListeningForMenuItemsByParentId(id: String) {
        folderRepository.stopListeningForFoldersByParentId(id)
        documentRepository.stopListeningForFoldersByParentId(id)
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

        documentRepository.refreshDocuments()
    }

    suspend fun saveDocument(document: Document) {
        documentRepository.saveDocument(document)
        documentRepository.refreshDocuments()
    }

    suspend fun deleteNotes(ids: Set<String>) {
        documentRepository.deleteDocumentByIds(ids)
    }

    suspend fun deleteFolderById(folderId: String) {
        documentRepository.deleteDocumentByFolder(folderId)
        folderRepository.deleteFolderById(folderId)
    }

    suspend fun favoriteNotes(ids: Set<String>) {
        documentRepository.favoriteDocumentByIds(ids)
    }

    suspend fun unFavoriteNotes(ids: Set<String>) {
        documentRepository.unFavoriteDocumentByIds(ids)
    }

    private fun listenForDocumentsByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Document>>> =
        documentRepository.listenForDocumentsByParentId(parentId, coroutineScope)

    /**
     * Listen and gets [MenuItem] groups by  parent folder.
     * This will return all folders that this method was called for (using the parentId).
     *
     * @param parentId The id of the folder
     * @param coroutineScope [CoroutineScope]
     */
    private fun listenForFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Folder>>> =
        folderRepository.listenForFoldersByParentId(parentId, coroutineScope)
}


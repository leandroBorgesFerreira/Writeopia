package io.writeopia.core.folders

import io.writeopia.models.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    suspend fun createFolder(folder: Folder)

    suspend fun updateFolder(folder: Folder)

    suspend fun setLastUpdated(folderId: String, long: Long)

    suspend fun deleteFolderById(folderId: String)

    suspend fun deleteFolderByParent(folderId: String)

    suspend fun favoriteDocumentByIds(ids: Set<String>)

    suspend fun unFavoriteDocumentByIds(ids: Set<String>)

    suspend fun moveToFolder(documentId: String, parentId: String)

    suspend fun refreshFolders()

    suspend fun getFolderById(id: String): Folder?

    suspend fun getFolderByParentId(parentId: String): List<Folder>

    suspend fun listenForFoldersByParentId(parentId: String): Flow<Map<String, List<Folder>>>

    suspend fun stopListeningForFoldersByParentId(parentId: String)
}

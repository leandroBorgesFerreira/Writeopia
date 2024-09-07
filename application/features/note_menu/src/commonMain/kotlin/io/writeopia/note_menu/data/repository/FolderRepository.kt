package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    suspend fun createFolder(folder: Folder)

    suspend fun updateFolder(folder: Folder)

    suspend fun deleteFolderById(folderId: String)

    suspend fun deleteFolderByParent(folderId: String)

    suspend fun moveToFolder(documentId: String, parentId: String)

    suspend fun refreshFolders()

    fun listenForFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Folder>>>

    suspend fun stopListeningForFoldersByParentId(parentId: String)
}

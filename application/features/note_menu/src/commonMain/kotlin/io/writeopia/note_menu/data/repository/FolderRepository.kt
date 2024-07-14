package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    suspend fun createFolder(folder: Folder)

    suspend fun updateFolder(folder: Folder)

    suspend fun deleteFolderById(folderId: String)

    fun refreshFolders()

    fun listenForAllFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Folder>>>
}

package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    suspend fun createFolder(folder: Folder)

    suspend fun getRootFolders(userId: String): List<Folder>

    suspend fun getChildrenFolders(userId: String, parentId: String): List<Folder>

    fun listenForAllFolders(): Flow<List<Folder>>
}

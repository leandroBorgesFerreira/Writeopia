package io.writeopia.sqldelight.dao

import io.writeopia.app.sql.FolderEntity
import io.writeopia.app.sql.FolderEntityQueries
import io.writeopia.sql.WriteopiaDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val ROOT_PATH = "root"

class FolderSqlDelightDao(database: WriteopiaDb?) {

    private val _foldersStateFlow = MutableStateFlow<List<FolderEntity>>(emptyList())

    private val folderEntityQueries: FolderEntityQueries? = database?.folderEntityQueries

    suspend fun createFolder(folder: FolderEntity) {
        folderEntityQueries?.insert(
            id = folder.id,
            parent_id = folder.parent_id,
            user_id = folder.user_id,
            title = folder.title,
            created_at = folder.created_at,
            last_updated_at = folder.last_updated_at,
            favorite = folder.favorite,
        ).also { refreshNotes() }
    }

    suspend fun updateFolder(folder: FolderEntity) {
        folderEntityQueries?.insert(
            id = folder.id,
            parent_id = folder.parent_id,
            user_id = folder.user_id,
            title = folder.title,
            created_at = folder.created_at,
            last_updated_at = folder.last_updated_at,
            favorite = folder.favorite,
        ).also { refreshNotes() }
    }

    fun listenForAllFolders(): Flow<List<FolderEntity>> {
        refreshNotes()
        return _foldersStateFlow
    }

    fun getRootFolders(userId: String): List<FolderEntity> = getFolders(ROOT_PATH, userId)

    fun getChildrenFolders(parentId: String, userId: String): List<FolderEntity> =
        getFolders(parentId, userId)

    suspend fun deleteFolder(folderId: String) {
        folderEntityQueries?.deleteFolder(folderId)
        refreshNotes()
    }

    private fun getFolders(parentId: String, userId: String): List<FolderEntity> =
        folderEntityQueries?.selectChildrenFolder(parentId, userId)
            ?.executeAsList()
            ?: emptyList()

    private fun refreshNotes() {
        folderEntityQueries?.selectAllFolders()?.executeAsList()?.let { folders ->
            _foldersStateFlow.value = folders
        }
    }
}

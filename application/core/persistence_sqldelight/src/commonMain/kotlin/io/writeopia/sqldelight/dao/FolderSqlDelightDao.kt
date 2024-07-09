package io.writeopia.sqldelight.dao

import io.writeopia.app.sql.FolderEntity
import io.writeopia.app.sql.FolderEntityQueries
import io.writeopia.sql.WriteopiaDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FolderSqlDelightDao(database: WriteopiaDb?) {

    private val _foldersStateFlow = MutableStateFlow<Map<String, List<FolderEntity>>>(emptyMap())

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
        )
        refreshNotes()
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
        )
        refreshNotes()
    }

    fun listenForFolderByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<FolderEntity>>> {
        coroutineScope.launch {
            SelectedIds.ids.add(parentId)
            refreshNotes()
        }
        return _foldersStateFlow
    }

    suspend fun getChildrenFolders(parentId: String): List<FolderEntity> =
        getFolders(parentId = parentId)

    suspend fun deleteFolder(folderId: String) {
        folderEntityQueries?.deleteFolder(folderId)
        refreshNotes()
    }

    private fun getFolders(parentId: String): List<FolderEntity> =
        folderEntityQueries?.selectChildrenFolder(parent_id = parentId)
            ?.executeAsList()
            ?: emptyList()

    private suspend fun refreshNotes() {
        _foldersStateFlow.value = SelectedIds.ids.associateWith(::getFolders)
    }
}

private object SelectedIds {
    val ids = mutableSetOf<String>()
}

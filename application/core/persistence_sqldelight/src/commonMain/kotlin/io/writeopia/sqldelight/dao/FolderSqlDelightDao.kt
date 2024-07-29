package io.writeopia.sqldelight.dao

import io.writeopia.app.sql.FolderEntity
import io.writeopia.app.sql.FolderEntityQueries
import io.writeopia.sql.WriteopiaDb
import io.writeopia.sqldelight.utils.sumValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
class FolderSqlDelightDao(database: WriteopiaDb?) {

    private val _foldersStateFlow =
        MutableStateFlow<Map<String, List<Pair<FolderEntity, Long>>>>(emptyMap())

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
        refreshFolders()
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
        refreshFolders()
    }

    fun listenForFolderByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Pair<FolderEntity, Long>>>> {
        coroutineScope.launch {
            SelectedIds.ids.add(parentId)
            refreshFolders()
        }

        return _foldersStateFlow
    }

    suspend fun deleteFolder(folderId: String) {
        folderEntityQueries?.deleteFolder(folderId)
        refreshFolders()
    }

    private suspend fun getFolders(parentId: String): List<Pair<FolderEntity, Long>> {
        val countMap = countAllItems()

        return folderEntityQueries?.selectChildrenFolder(parent_id = parentId)
            ?.executeAsList()
            ?.map { folderEntity ->
                folderEntity to (countMap[folderEntity.id] ?: 0)
            } ?: emptyList()
    }


    private fun countAllItems(): Map<String, Long> {
        val foldersCount = folderEntityQueries?.countAllFolderItems()
            ?.executeAsList()
            ?.associate { countByParent ->
                countByParent.parent_id to countByParent.COUNT
            } ?: emptyMap()

        val documentsCount = folderEntityQueries?.countAllDocumentItems()
            ?.executeAsList()
            ?.associate { countByParent ->
                countByParent.parent_document_id to countByParent.COUNT
            } ?: emptyMap()

        return foldersCount.sumValues(documentsCount)
    }

    suspend fun refreshFolders() {
        _foldersStateFlow.value = SelectedIds.ids.associateWith {
            getFolders(it)
        }
    }

    suspend fun removeListening(id: String) {
        SelectedIds.ids.remove(id)
        refreshFolders()
    }

    suspend fun moveToFolder(documentId: String, parentId: String) {
        folderEntityQueries?.moveToFolder(parentId, documentId)
    }
}


private object SelectedIds {
    val ids = mutableSetOf<String>()
}

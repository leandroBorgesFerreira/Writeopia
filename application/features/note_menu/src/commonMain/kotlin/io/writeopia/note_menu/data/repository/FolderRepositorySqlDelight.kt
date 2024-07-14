package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.extensions.toEntity
import io.writeopia.note_menu.extensions.toModel
import io.writeopia.sqldelight.dao.FolderSqlDelightDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderRepositorySqlDelight(
    private val folderDao: FolderSqlDelightDao
) : FolderRepository {

    override suspend fun createFolder(folder: Folder) {
        folderDao.createFolder(folder.toEntity())
    }

    override suspend fun updateFolder(folder: Folder) {
        folderDao.updateFolder(folder.toEntity())
    }

    override fun listenForAllFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Folder>>> {
        return folderDao.listenForFolderByParentId(parentId, coroutineScope)
            .map { folderEntityMap ->
                folderEntityMap.mapValues { (_, folderEntityListWithCount) ->
                    folderEntityListWithCount.map { (folderEntity, count) ->
                        folderEntity.toModel(count)
                    }
                }
            }
    }

    override suspend fun deleteFolderById(folderId: String) {
        folderDao.deleteFolder(folderId)
    }

    override fun refreshFolders() {
        folderDao.refreshFolders()
    }
}
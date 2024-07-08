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

    override suspend fun getChildrenFolders(userId: String, parentId: String): List<Folder> =
        folderDao.getChildrenFolders(parentId = parentId).map { folderEntity ->
            folderEntity.toModel()
        }

    override fun listenForAllFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Folder>>> =
        folderDao.listenForFolderByParentId(parentId, coroutineScope)
            .map { folderEntityList ->
                folderEntityList.mapValues { (_, folderEntityList) ->
                    folderEntityList.map { it.toModel() }
                }
            }

    override suspend fun deleteFolderById(folderId: String) {
        folderDao.deleteFolder(folderId)
    }
}

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

    override suspend fun getFolderById(id: String): Folder? =
        folderDao.getFolderById(id)?.toModel(0)

    override suspend fun createFolder(folder: Folder) {
        folderDao.createFolder(folder.toEntity())
    }

    override suspend fun updateFolder(folder: Folder) {
        folderDao.updateFolder(folder.toEntity())
    }

    override suspend fun setLastUpdated(folderId: String, long: Long) {
        folderDao.setLastUpdate(folderId, long)
    }

    override suspend fun favoriteDocumentByIds(ids: Set<String>) {
        ids.forEach { id ->
            folderDao.favoriteById(id)
        }

        refreshFolders()
    }

    override suspend fun unFavoriteDocumentByIds(ids: Set<String>) {
        ids.forEach { id ->
            folderDao.unFavoriteById(id)
        }

        refreshFolders()
    }

    override suspend fun getFolderByParentId(parentId: String): List<Folder> =
        folderDao.getFoldersByParentId(parentId)
            .map { (folderEntity, count) -> folderEntity.toModel(count) }

    override fun listenForFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope?
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

    override suspend fun stopListeningForFoldersByParentId(parentId: String) {
        folderDao.removeListening(parentId)
    }

    override suspend fun deleteFolderById(folderId: String) {
        folderDao.deleteFolder(folderId)
    }

    override suspend fun deleteFolderByParent(folderId: String) {
        folderDao.deleteFolderByParent(folderId)
    }

    override suspend fun refreshFolders() {
        folderDao.refreshFolders()
    }

    override suspend fun moveToFolder(documentId: String, parentId: String) {
        folderDao.moveToFolder(documentId = documentId, parentId = parentId)
        refreshFolders()
    }
}

package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder
import io.writeopia.note_menu.extensions.toModel
import io.writeopia.note_menu.extensions.toRoomEntity
import io.writeopia.persistence.room.data.daos.FolderRoomDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class RoomFolderRepository(private val folderRoomDao: FolderRoomDao) : FolderRepository {

    override suspend fun createFolder(folder: Folder) {
        updateFolder(folder)
    }

    override suspend fun updateFolder(folder: Folder) {
        folderRoomDao.upsertFolder(folder.toRoomEntity())
    }

    override suspend fun setLastUpdated(folderId: String, long: Long) {
        updateFolderById(folderId) { folder ->
            folder.copy(lastUpdatedAt = Instant.fromEpochMilliseconds(long))
        }
    }

    override suspend fun deleteFolderById(folderId: String) {
        folderRoomDao.deleteById(folderId)
    }

    override suspend fun deleteFolderByParent(folderId: String) {
        folderRoomDao.getFolderByParentId(folderId)
    }

    override suspend fun favoriteDocumentByIds(ids: Set<String>) {
        ids.forEach { folderId ->
            updateFolderById(folderId) { folder ->
                folder.copy(favorite = true)
            }
        }
    }

    override suspend fun unFavoriteDocumentByIds(ids: Set<String>) {
        ids.forEach { folderId ->
            updateFolderById(folderId) { folder ->
                folder.copy(favorite = false)
            }
        }
    }

    override suspend fun moveToFolder(documentId: String, parentId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshFolders() {}

    override suspend fun getFolderById(id: String): Folder? =
        folderRoomDao.getFolderById(id)?.toModel(0)

    override suspend fun getFolderByParentId(parentId: String): List<Folder> =
        folderRoomDao.getFolderByParentId(parentId).map { folderEntity ->
            folderEntity.toModel(0)
        }

    override fun listenForFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope?
    ): Flow<Map<String, List<Folder>>> {
        val flows = SelectedIds.ids.map {
            folderRoomDao.listenForFolderByParentId(parentId)
        }

        return combine(flows) { arrayOfFolders -> arrayOfFolders.toList().flatten() }
            .map { folders ->
                folders.map { folderEntity ->
                    folderEntity.toModel(0)
                }.groupBy { folder -> folder.parentId }
            }
    }


    override suspend fun stopListeningForFoldersByParentId(parentId: String) {
        SelectedIds.ids.remove(parentId)
    }

    private suspend fun updateFolderById(id: String, func: (Folder) -> Folder) {
        folderRoomDao.getFolderById(id = id)
            ?.toModel(0)
            ?.let(func)
            ?.let { folder ->
                updateFolder(folder)
            }
    }
}

private object SelectedIds {
    val ids = mutableSetOf<String>()
}

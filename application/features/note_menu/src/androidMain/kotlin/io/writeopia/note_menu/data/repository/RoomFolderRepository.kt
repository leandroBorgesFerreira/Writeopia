package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class RoomFolderRepository: FolderRepository {

    override suspend fun createFolder(folder: Folder) {
        TODO("Not yet implemented")
    }

    override suspend fun updateFolder(folder: Folder) {
        TODO("Not yet implemented")
    }

    override suspend fun setLasUpdated(folderId: String, long: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFolderById(folderId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFolderByParent(folderId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun favoriteDocumentByIds(ids: Set<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun unFavoriteDocumentByIds(ids: Set<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun moveToFolder(documentId: String, parentId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshFolders() {
        TODO("Not yet implemented")
    }

    override suspend fun getFolderById(id: String): Folder? {
        TODO("Not yet implemented")
    }

    override suspend fun getFolderByParentId(parentId: String): List<Folder> {
        TODO("Not yet implemented")
    }

    override fun listenForFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Folder>>> {
        TODO("Not yet implemented")
    }

    override suspend fun stopListeningForFoldersByParentId(parentId: String) {
        TODO("Not yet implemented")
    }
}

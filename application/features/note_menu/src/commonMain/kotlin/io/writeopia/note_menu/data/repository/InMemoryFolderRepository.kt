package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class InMemoryFolderRepository : FolderRepository {

    private val mutableMap = mutableMapOf<String, Folder>()
    private val _foldersStateFlow = MutableStateFlow<Map<String, Folder>>(mutableMap)

    override suspend fun createFolder(folder: Folder) {
        mutableMap[folder.id] = folder
        refreshState()
    }

    override suspend fun updateFolder(folder: Folder) {
        mutableMap[folder.id] = folder
        refreshState()
    }

    override fun listenForFoldersByParentId(
        parentId: String,
        coroutineScope: CoroutineScope
    ): Flow<Map<String, List<Folder>>> {
        return MutableStateFlow(emptyMap())
    }

    override suspend fun deleteFolderById(folderId: String) {
        mutableMap.remove(folderId)
        refreshState()
    }

    override suspend fun refreshFolders() {
        TODO("Not yet implemented")
    }

    override suspend fun moveToFolder(documentId: String, parentId: String) {
        TODO("Not yet implemented")
    }

    private fun refreshState() {
        _foldersStateFlow.value = mutableMap
    }

    companion object {
        private var instance: InMemoryFolderRepository? = null

        fun singleton(): InMemoryFolderRepository {
            return instance ?: run {
                instance = InMemoryFolderRepository()
                instance!!
            }
        }
    }

    override suspend fun stopListeningForFoldersByParentId(parentId: String) {

    }


}

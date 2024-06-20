package io.writeopia.note_menu.data.repository

import io.writeopia.note_menu.data.model.Folder

interface FolderRepository {

    suspend fun getRootFolders(userId: String): List<Folder>

    suspend fun getChildrenFolders(userId: String, parentId: String): List<Folder>
}

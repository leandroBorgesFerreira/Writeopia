package io.writeopia.sqldelight.dao

import io.writeopia.app.sql.FolderEntity
import io.writeopia.app.sql.FolderEntityQueries
import io.writeopia.sql.WriteopiaDb

private const val ROOT_PATH = "root"

class FolderSqlDelightDao(database: WriteopiaDb?) {

    private val folderEntityQueries: FolderEntityQueries? = database?.folderEntityQueries

    suspend fun createFolder(folder: FolderEntity) {
        folderEntityQueries?.insert(
            id = folder.id,
            name = folder.name,
            parent_id = folder.parent_id,
            user_id = folder.user_id,
            title = folder.title,
            created_at = folder.created_at,
            last_updated_at = folder.last_updated_at,
            favorite = folder.favorite,
        )
    }

    fun listenForAllFolders()

    fun getRootFolders(userId: String): List<FolderEntity> = getFolders(ROOT_PATH, userId)

    fun getChildrenFolders(parentId: String, userId: String): List<FolderEntity> =
        getFolders(parentId, userId)

    private fun getFolders(parentId: String, userId: String): List<FolderEntity> =
        folderEntityQueries?.selectChildrenFolder(parentId, userId)
            ?.executeAsList()
            ?: emptyList()
}

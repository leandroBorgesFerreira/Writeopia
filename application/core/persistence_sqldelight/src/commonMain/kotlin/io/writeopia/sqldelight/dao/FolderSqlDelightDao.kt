package io.writeopia.sqldelight.dao

import io.writeopia.app.sql.FolderEntity
import io.writeopia.app.sql.FolderEntityQueries
import io.writeopia.sql.WriteopiaDb

private const val ROOT_PATH = "root"

class FolderSqlDelightDao(database: WriteopiaDb?) {

    private val folderEntityQueries: FolderEntityQueries? = database?.folderEntityQueries

    fun getRootFolders(userId: String): List<FolderEntity> = getFolders(ROOT_PATH, userId)

    fun getChildrenFolders(parentId: String, userId: String): List<FolderEntity> =
        getFolders(parentId, userId)

    private fun getFolders(parentId: String, userId: String): List<FolderEntity> =
        folderEntityQueries?.selectChildrenFolder(parentId, userId)
            ?.executeAsList()
            ?: emptyList()
}

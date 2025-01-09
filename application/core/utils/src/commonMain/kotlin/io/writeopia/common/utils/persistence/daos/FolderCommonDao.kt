package io.writeopia.common.utils.persistence.daos

import io.writeopia.common.utils.persistence.FolderCommonEntity
import kotlinx.coroutines.flow.Flow

interface FolderCommonDao {
    suspend fun upsertFolder(folderEntity: FolderCommonEntity)

    suspend fun getFolderById(id: String): FolderCommonEntity?

    suspend fun search(query: String): List<FolderCommonEntity>

    suspend fun getLastUpdated(): List<FolderCommonEntity>

    suspend fun getFolderByParentId(id: String): List<FolderCommonEntity>

    fun listenForFolderByParentId(id: String): Flow<List<FolderCommonEntity>>

    suspend fun deleteById(id: String): Int

    suspend fun deleteByParentId(id: String): Int
}

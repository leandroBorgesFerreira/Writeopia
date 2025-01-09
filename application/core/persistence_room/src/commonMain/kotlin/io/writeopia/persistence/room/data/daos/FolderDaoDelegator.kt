package io.writeopia.persistence.room.data.daos

import io.writeopia.common.utils.persistence.FolderCommonEntity
import io.writeopia.common.utils.persistence.daos.FolderCommonDao
import io.writeopia.persistence.room.extensions.toEntity
import io.writeopia.persistence.room.extensions.toCommonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderDaoDelegator(
    private val delegate: FolderRoomDao
) : FolderCommonDao {

    override suspend fun upsertFolder(folderEntity: FolderCommonEntity) {
        delegate.upsertFolder(folderEntity.toEntity())
    }

    override suspend fun getFolderById(id: String): FolderCommonEntity? {
        return delegate.getFolderById(id)?.toCommonEntity()
    }

    override suspend fun search(query: String): List<FolderCommonEntity> {
        return delegate.search(query).map { it.toCommonEntity() }
    }

    override suspend fun getLastUpdated(): List<FolderCommonEntity> {
        return delegate.getLastUpdated().map { it.toCommonEntity() }
    }

    override suspend fun getFolderByParentId(id: String): List<FolderCommonEntity> {
        return delegate.getFolderByParentId(id).map { it.toCommonEntity() }
    }

    override fun listenForFolderByParentId(id: String): Flow<List<FolderCommonEntity>> {
        return delegate.listenForFolderByParentId(id).map { list ->
            list.map { it.toCommonEntity() }
        }
    }

    override suspend fun deleteById(id: String): Int {
        return delegate.deleteById(id)
    }

    override suspend fun deleteByParentId(id: String): Int {
        return delegate.deleteByParentId(id)
    }
}

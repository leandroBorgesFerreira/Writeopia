package io.writeopia.persistence.room.data.daos

import io.writeopia.common.utils.persistence.configuration.NotesConfigurationCommonEntity
import io.writeopia.common.utils.persistence.daos.NotesConfigurationCommonDao
import io.writeopia.persistence.room.extensions.toEntity
import io.writeopia.persistence.room.extensions.toCommonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesConfigurationRoomDaoDelegator(
    private val delegate: NotesConfigurationRoomDao
) : NotesConfigurationCommonDao {

    override suspend fun saveConfiguration(configuration: NotesConfigurationCommonEntity) {
        delegate.saveConfiguration(configuration.toEntity())
    }

    override suspend fun getConfigurationByUserId(userId: String): NotesConfigurationCommonEntity? {
        return delegate.getConfigurationByUserId(userId)?.toCommonEntity()
    }

    override fun listenForConfigurationByUserId(userId: String): Flow<NotesConfigurationCommonEntity?> {
        return delegate.listenForConfigurationByUserId(userId).map { it?.toCommonEntity() }
    }
}


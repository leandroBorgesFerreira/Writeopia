package io.writeopia.persistence.room.data.daos

import io.writeopia.common.utils.persistence.NotesConfigurationModel
import io.writeopia.common.utils.persistence.daos.NotesConfigurationCommonDao
import io.writeopia.persistence.room.extensions.toEntity
import io.writeopia.persistence.room.extensions.toCommonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesConfigurationRoomDaoDelegator(
    private val delegate: NotesConfigurationRoomDao
) : NotesConfigurationCommonDao {

    override suspend fun saveConfiguration(configuration: NotesConfigurationModel) {
        delegate.saveConfiguration(configuration.toEntity())
    }

    override suspend fun getConfigurationByUserId(userId: String): NotesConfigurationModel? {
        return delegate.getConfigurationByUserId(userId)?.toCommonEntity()
    }

    override fun listenForConfigurationByUserId(userId: String): Flow<NotesConfigurationModel?> {
        return delegate.listenForConfigurationByUserId(userId).map { it?.toCommonEntity() }
    }
}


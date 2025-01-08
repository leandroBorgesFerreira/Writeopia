package io.writeopia.persistence.room.data.daos

import io.writeopia.persistence.room.extensions.toCommon
import io.writeopia.persistence.room.extensions.toRoom
import io.writeopia.repository.UiConfigurationDao
import io.writeopia.repository.entity.UiConfigurationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UiConfigurationCommonDao(
    private val uiConfigurationRoomDao: UiConfigurationRoomDao
) : UiConfigurationDao {

    override suspend fun saveUiConfiguration(configuration: UiConfigurationEntity) {
        uiConfigurationRoomDao.saveUiConfiguration(configuration.toRoom())
    }

    override suspend fun getConfigurationByUserId(userId: String): UiConfigurationEntity? =
        uiConfigurationRoomDao.getConfigurationByUserId(userId)?.toCommon()

    override fun listenForConfigurationByUserId(userId: String): Flow<UiConfigurationEntity?> =
        uiConfigurationRoomDao.listenForConfigurationByUserId(userId).map {
            it?.toCommon()
        }
}

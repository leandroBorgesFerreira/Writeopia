package io.writeopia.repository

import io.writeopia.repository.entity.UiConfigurationEntity
import kotlinx.coroutines.flow.Flow

interface UiConfigurationDao {

    suspend fun saveUiConfiguration(configuration: UiConfigurationEntity)

    suspend fun getConfigurationByUserId(userId: String): UiConfigurationEntity?

    fun listenForConfigurationByUserId(userId: String): Flow<UiConfigurationEntity?>
}

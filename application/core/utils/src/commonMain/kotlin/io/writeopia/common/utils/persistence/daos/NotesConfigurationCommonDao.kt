package io.writeopia.common.utils.persistence.daos

import io.writeopia.common.utils.persistence.configuration.NotesConfigurationCommonEntity
import kotlinx.coroutines.flow.Flow

interface NotesConfigurationCommonDao {
    suspend fun saveConfiguration(configuration: NotesConfigurationCommonEntity)

    suspend fun getConfigurationByUserId(userId: String): NotesConfigurationCommonEntity?

    fun listenForConfigurationByUserId(userId: String): Flow<NotesConfigurationCommonEntity?>
}

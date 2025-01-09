package io.writeopia.common.utils.persistence.daos

import io.writeopia.common.utils.persistence.NotesConfigurationModel
import kotlinx.coroutines.flow.Flow

interface NotesConfigurationCommonDao {
    suspend fun saveConfiguration(configuration: NotesConfigurationModel)

    suspend fun getConfigurationByUserId(userId: String): NotesConfigurationModel?

    fun listenForConfigurationByUserId(userId: String): Flow<NotesConfigurationModel?>
}

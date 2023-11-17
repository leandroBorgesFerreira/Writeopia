package io.writeopia.persistence.room.data.daos

import io.writeopia.persistence.room.data.entities.NotesConfigurationEntity

interface NotesConfigurationDao {

    suspend fun saveConfiguration(configuration: NotesConfigurationEntity)

    suspend fun getConfigurationByUserId(userId: String): NotesConfigurationEntity?
}
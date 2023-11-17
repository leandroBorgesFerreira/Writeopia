package io.writeopia.persistence.data.daos

import androidx.room.Query
import io.writeopia.persistence.data.entities.NotesConfigurationEntity

interface NotesConfigurationDao {

    suspend fun saveConfiguration(configuration: NotesConfigurationEntity)

    suspend fun getConfiguration(): NotesConfigurationEntity?
}
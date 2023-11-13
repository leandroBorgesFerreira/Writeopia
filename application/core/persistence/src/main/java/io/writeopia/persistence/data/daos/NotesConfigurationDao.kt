package io.writeopia.persistence.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.writeopia.persistence.data.entities.NotesConfigurationEntity

@Dao
interface NotesConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfiguration(configuration: NotesConfigurationEntity)

    @Query("SELECT * FROM notes_configuration LIMIT 1")
    suspend fun getConfiguration(): NotesConfigurationEntity?
}
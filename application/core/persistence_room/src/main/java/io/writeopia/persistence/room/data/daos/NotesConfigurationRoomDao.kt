package io.writeopia.persistence.room.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.writeopia.persistence.room.data.entities.NotesConfigurationEntity

@Dao
interface NotesConfigurationRoomDao: NotesConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun saveConfiguration(configuration: NotesConfigurationEntity)

    @Query("SELECT * FROM notes_configuration WHERE notes_configuration.user_id = :userId LIMIT 1")
    override suspend fun getConfigurationByUserId(userId: String): NotesConfigurationEntity?
}
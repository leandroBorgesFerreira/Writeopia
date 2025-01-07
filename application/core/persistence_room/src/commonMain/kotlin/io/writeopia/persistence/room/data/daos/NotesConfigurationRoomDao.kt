package io.writeopia.persistence.room.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.writeopia.persistence.room.data.entities.NOTES_CONFIGURATION
import io.writeopia.persistence.room.data.entities.NotesConfigurationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesConfigurationRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfiguration(configuration: NotesConfigurationEntity)

    @Query("SELECT * FROM $NOTES_CONFIGURATION WHERE $NOTES_CONFIGURATION.user_id = :userId LIMIT 1")
    suspend fun getConfigurationByUserId(userId: String): NotesConfigurationEntity?

    @Query("SELECT * FROM $NOTES_CONFIGURATION WHERE $NOTES_CONFIGURATION.user_id = :userId LIMIT 1")
    fun listenForConfigurationByUserId(userId: String): Flow<NotesConfigurationEntity?>
}

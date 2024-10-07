package io.writeopia.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.writeopia.repository.entity.UiConfigurationEntity
//import io.writeopia.repository.entity.UiConfigurationRoomEntity
import kotlinx.coroutines.flow.Flow

//@Dao
interface UiConfigurationRoomDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUiConfiguration(configuration: UiConfigurationEntity)
//
//    @Query("SELECT * FROM ui_configuration_room WHERE ui_configuration_room.user_id = :userId LIMIT 1")
    suspend fun getConfigurationByUserId(userId: String): UiConfigurationEntity?
//
//    @Query("SELECT * FROM ui_configuration_room WHERE ui_configuration_room.user_id = :userId LIMIT 1")
    fun listenForConfigurationByUserId(userId: String): Flow<UiConfigurationEntity?>
}

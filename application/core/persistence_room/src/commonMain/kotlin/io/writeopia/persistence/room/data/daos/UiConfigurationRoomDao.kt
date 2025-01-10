package io.writeopia.persistence.room.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.writeopia.persistence.room.data.entities.UI_CONFIGURATION_TABLE
import io.writeopia.persistence.room.data.entities.UiConfigurationRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UiConfigurationRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUiConfiguration(configuration: UiConfigurationRoomEntity)

    @Query("select * FROM $UI_CONFIGURATION_TABLE WHERE $UI_CONFIGURATION_TABLE.userId = :userId")
    suspend fun getConfigurationByUserId(userId: String): UiConfigurationRoomEntity?

    @Query("select * FROM $UI_CONFIGURATION_TABLE WHERE $UI_CONFIGURATION_TABLE.userId = :userId")
    fun listenForConfigurationByUserId(userId: String): Flow<UiConfigurationRoomEntity?>

}
